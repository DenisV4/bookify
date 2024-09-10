package example.bookify.server.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import example.bookify.server.security.dto.AuthTokens;
import example.bookify.server.security.dto.ClientInfo;
import example.bookify.server.web.dto.request.LoginRequest;
import example.bookify.server.web.dto.request.UserUpsertRequest;
import example.bookify.server.web.dto.response.AuthResponse;

public interface SecurityService {

    AuthTokens authenticate(LoginRequest loginRequest, ClientInfo clientInfo);

    void register(UserUpsertRequest request);

    Boolean validateName(String name);

    Boolean validateEmail(String email);

    void logout();

    AuthTokens refreshAuthentication(String token);
}
