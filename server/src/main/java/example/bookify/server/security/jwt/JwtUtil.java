package example.bookify.server.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import example.bookify.server.security.AppUserDetails;
import example.bookify.server.security.dto.AuthSubject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private SecretKey secretKey;

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.accessTokenExpiration}")
    private Duration accessTokenExpiration;

    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha256.digest(jwtSecret.getBytes());
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        this.objectMapper = new ObjectMapper();
    }

    public String generateJwtToken(AppUserDetails userDetails) throws JsonProcessingException {
        var authSubject = AuthSubject.builder()
                .id(userDetails.getId())
                .name(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
                )
                .build();

        var authSubjectJson = objectMapper.writeValueAsString(authSubject);

        return Jwts.builder()
                .subject(authSubjectJson)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + accessTokenExpiration.toMillis()))
                .signWith(secretKey)
                .compact();
    }

    public String getUserName(String token) throws JsonProcessingException {
        var subject =  Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        var authSubject = objectMapper.readValue(subject, AuthSubject.class);
        return authSubject.getName();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (ExpiredJwtException exc) {
            log.debug("Token is expired: {}", exc.getMessage());
        } catch (UnsupportedJwtException exc) {
            log.debug("Token is unsupported: {}", exc.getMessage());
        } catch (MalformedJwtException exc) {
            log.debug("Invalid token: {}", exc.getMessage());
        } catch (IllegalArgumentException exc) {
            log.debug("Claims string is empty: {}", exc.getMessage());
        }

        return false;
    }
}
