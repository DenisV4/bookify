package example.bookify.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import example.bookify.server.security.SecurityService;
import example.bookify.server.security.dto.ClientInfo;
import example.bookify.server.validator.group.OnRegister;
import example.bookify.server.web.dto.request.LoginRequest;
import example.bookify.server.web.dto.request.UserUpsertRequest;
import example.bookify.server.web.dto.response.AuthResponse;
import example.bookify.server.web.dto.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API endpoints. Login, register, logout refresh token and validate.")
public class AuthController {

    @Value("${app.security.jwt.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    private final SecurityService securityService;

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Login with username and password. Returns user id, access token and user roles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = AuthResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    public AuthResponse login(@RequestBody
                              LoginRequest loginRequest,
                              HttpServletRequest request,
                              HttpServletResponse response) {

        var remoteAddress = request.getHeader("X-Forwarded-For");

        if (remoteAddress == null || remoteAddress.isEmpty()) {
            remoteAddress = request.getRemoteAddr();
        } else {
            remoteAddress = remoteAddress.split(",")[0].trim();
        }

        var clientInfo = ClientInfo.builder()
                .userAgent(request.getHeader("user-Agent"))
                .remoteAddress(remoteAddress)
                .build();

        var tokens = securityService.authenticate(loginRequest, clientInfo);
        setRefreshTokenCookie(tokens.getRefreshToken(), response);

        return AuthResponse.builder()
                .accessToken(tokens.getAccessToken())
                .build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnRegister.class)
    @Operation(
            summary = "Register",
            description = "Register new user. Returns no content."
    )
    public void register(@RequestBody @Valid UserUpsertRequest request) {
        securityService.register(request);
    }

    @GetMapping("/validate-name")
    @Operation(
            summary = "Validate name",
            description = "Validate name. Returns true if name is valid."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "text/plain")}
            )
    })
    public Boolean validateName(String name) {
        return securityService.validateName(name);
    }

    @GetMapping("/validate-email")
    @Operation(
            summary = "Validate email",
            description = "Validate email. Returns true if email is valid."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "text/plain")}
            )
    })
    public Boolean validateEmail(String email) {
        return securityService.validateEmail(email);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Logout",
            description = "Logout. Returns no content."
    )
    public void logout(HttpServletResponse response) {
        securityService.logout();
        removeRefreshTokenCookie(response);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh token",
            description = "Refresh token. Returns user id, access token and user roles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = AuthResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    public AuthResponse refresh(@CookieValue(name = "${app.security.jwt.refreshTokenCookieName}", required = false)
                                String token,
                                HttpServletResponse response) {

        var tokens = securityService.refreshAuthentication(token);
        setRefreshTokenCookie(tokens.getRefreshToken(), response);

        return AuthResponse.builder()
                .accessToken(tokens.getAccessToken())
                .build();
    }

    private void setRefreshTokenCookie(String token, HttpServletResponse response) {
        var cookie = new Cookie(refreshTokenCookieName, token);

        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    private void removeRefreshTokenCookie(HttpServletResponse response) {
        var cookie = new Cookie(refreshTokenCookieName, null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
