package example.bookify.server.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import example.bookify.server.event.AuthenticationEvent;
import example.bookify.server.event.UserRegistrationEvent;
import example.bookify.server.exception.RefreshTokenException;
import example.bookify.server.mapper.UserMapper;
import example.bookify.server.mapper.UserRoleMapper;
import example.bookify.server.model.RefreshToken;
import example.bookify.server.model.UserRole;
import example.bookify.server.security.dto.AuthTokens;
import example.bookify.server.security.dto.ClientInfo;
import example.bookify.server.security.jwt.JwtUtil;
import example.bookify.server.service.RefreshTokenService;
import example.bookify.server.service.UserService;
import example.bookify.server.web.dto.request.LoginRequest;
import example.bookify.server.web.dto.request.UserUpsertRequest;
import example.bookify.server.web.dto.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ua_parser.Parser;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppSecurityService implements SecurityService {

    @Value("${app.kafka.topic.statistics.authentication}")
    private String authenticationTopic;

    @Value("${app.kafka.topic.statistics.user-registration}")
    private String registrationTopic;

    private Parser uaParser;

    private final KafkaTemplate<String, AuthenticationEvent> authenticationKafkaTemplate;
    private final KafkaTemplate<String, UserRegistrationEvent> registrationKafkaTemplate;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    @PostConstruct
    public void init() {
        uaParser = new Parser();
    }

    @Override
    public AuthTokens authenticate(LoginRequest loginRequest, ClientInfo clientInfo) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getName(), loginRequest.getPassword()
        );
        var authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userDetails = (AppUserDetails) authentication.getPrincipal();
        var refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        var accessToken = provideAccessToken(userDetails);

        var client = uaParser.parse(clientInfo.getUserAgent());

        var authenticationEvent = AuthenticationEvent.builder()
                .userId(userDetails.getId())
                .name(userDetails.getUsername())
                .device(client.device != null
                        ? client.device.family
                        : "Unknown"
                )
                .os(
                        client.os != null
                                ? client.os.family + " " + client.os.major + "." + client.os.minor
                                : "Unknown")
                .browser(
                        client.userAgent != null
                                ? client.userAgent.family + " " + client.userAgent.major + "." + client.userAgent.minor
                                : "Unknown"
                )
                .remoteAddress(clientInfo.getRemoteAddress())
                .build();

        authenticationKafkaTemplate.send(authenticationTopic, authenticationEvent);

        return AuthTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public void register(UserUpsertRequest request) {
        var roles = userRoleMapper.roleTypeListToRoleList(List.of(UserRole.RoleType.ROLE_USER));
        var user = userService.save(userMapper.requestToUser(request), roles);

        registrationKafkaTemplate.send(registrationTopic, userMapper.userToUserRegistrationEvent(user));
    }

    @Override
    public Boolean validateName(String name) {
        return !userService.existsByName(name);
    }

    @Override
    public Boolean validateEmail(String email) {
        return !userService.existsByEmail(email);
    }

    @Override
    public void logout() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUserDetails appUserDetails) {
            refreshTokenService.deleteByUserId(appUserDetails.getId());
        }
    }

    @Override
    public AuthTokens refreshAuthentication(String token) {
        if (token == null) {
            throw new RefreshTokenException("Token is missing");
        }

        return refreshTokenService.findByToken(token)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    var tokenOwner = userService.findById(userId);
                    var userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(tokenOwner.getName());

                    var newAccessToken = provideAccessToken(userDetails);
                    var newRefreshToken = refreshTokenService.createRefreshToken(userId);

                    return AuthTokens.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .build();
                })
                .orElseThrow(RefreshTokenException.supply(token, "Token not found"));
    }

    private String provideAccessToken(AppUserDetails userDetails) {
        try {
            return jwtUtil.generateJwtToken(userDetails);
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }
}
