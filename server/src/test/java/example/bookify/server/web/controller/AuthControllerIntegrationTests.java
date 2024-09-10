package example.bookify.server.web.controller;

import jakarta.servlet.http.Cookie;
import example.bookify.server.model.RefreshToken;
import example.bookify.server.model.User;
import example.bookify.server.model.UserRole;
import example.bookify.server.repository.RefreshTokenRepository;
import example.bookify.server.repository.UserRepository;
import example.bookify.server.web.dto.response.AuthResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("AuthController Integration Tests")
@Sql(
        scripts = "classpath:db/init.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
    statements = {"TRUNCATE TABLE users RESTART IDENTITY CASCADE;"},
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class AuthControllerIntegrationTests extends AbstractWebControllerTest {

    @Value("${app.security.jwt.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        var user = User.builder()
                .id(1L)
                .name("testUser")
                .email("test@me.com")
                .password(passwordEncoder.encode("password"))
                .roles(List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_USER).build()))
                .build();

        var role = UserRole.builder()
                .user(user)
                .authority(UserRole.RoleType.ROLE_USER).build();

        user.setRoles(List.of(role));

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName(
            "Should return OK response with access token and refresh token cookie " +
                    "when auth request contains valid credentials"
    )
    void shouldReturnOKAndAuthResponseWhenUserIsAuthenticated() throws Exception {
        var loginRequest_json = """
                    {
                        "name": "testUser",
                        "password": "password"
                    }
                """;

        var response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest_json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var refreshTokenCookie = response.getCookie(refreshTokenCookieName);
        assertThat(refreshTokenCookie).isNotNull();
        assertThat(refreshTokenCookie.isHttpOnly()).isTrue();
        assertThat(refreshTokenCookie.getValue()).isNotEmpty();

        var authentication = (AuthResponse) parseResponse(AuthResponse.class, response.getContentAsString());
        assertThat(authentication.getAccessToken()).isNotEmpty();

    }

    @Test
    @DisplayName("Should return UNAUTHORIZED response when auth request contains invalid credentials")
    void shouldReturn401ResponseWhenUserIsNotAuthenticated() throws Exception {
        var loginRequest_json = """
                    {
                        "name": "testUser",
                        "password": "invalid"
                    }
                """;

        var response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest_json))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).contains("Bad credentials");
    }

    @Test
    @DisplayName("Should return CREATED and create new user when register request contains unique email and name")
    void shouldReturnCreatedWhenEmailAndNameAreUnique() throws Exception {
        var registerRequest_json = """
                    {
                        "email": "newUser@me.com",
                        "name": "newUser",
                        "password": "password"
                    }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest_json))
                .andExpect(status().isCreated());

        var createdUser = userRepository.findByName("newUser");

        assertThat(createdUser).isPresent();
        assertThat(createdUser.get().getName()).isEqualTo("newUser");
        assertThat(createdUser.get().getEmail()).isEqualTo("newUser@me.com");
        assertThat(createdUser.get().getRoles()).hasSize(1);
        assertThat(createdUser.get().getRoles().get(0).getAuthority()).isEqualTo(UserRole.RoleType.ROLE_USER);
    }

    @Test
    @DisplayName("Should return BAD_REQUEST when register request contains non-unique name")
    void shouldReturnBadRequestWhenNameIsNotUnique() throws Exception {
        var registerRequest_json = """
                    {
                        "email": "newUser@me.com",
                        "name": "testUser",
                        "password": "password"
                    }
                """;

        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest_json))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).contains("User with name `testUser` already exists");
    }

    @Test
    @DisplayName("Should return BAD_REQUEST when register request contains non-unique email")
    void shouldReturnBadRequestWhenEmailIsNotUnique() throws Exception {
        var registerRequest_json = """
                    {
                        "email": "test@me.com",
                        "name": "newUser",
                        "password": "password"
                    }
                """;

        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest_json))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).contains("User with email `test@me.com` already exists");
    }

    @Nested
    @ActiveProfiles({"test", "test-user-details"})
    @DisplayName("Authentication change tests")
    class AuthenticationChangeTests {

        @Test
        @DisplayName("Should return NOCONTENT and remove refresh token from db and cookie when logout")
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldReturnNoContentWhenLogout() throws Exception {
            var token = UUID.randomUUID().toString();

            var refreshToken = RefreshToken.builder()
                    .userId(1L)
                    .token(token)
                    .expirationTime(LocalDateTime.now().plusMinutes(1))
                    .build();

            var cookie = new Cookie(refreshTokenCookieName, token);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            refreshTokenRepository.save(refreshToken);
            assertThat(refreshTokenRepository.count()).isEqualTo(1);

            var response = mockMvc.perform(post("/api/auth/logout")
                            .cookie(cookie))
                    .andExpect(status().isNoContent())
                    .andReturn()
                    .getResponse();

            var refreshTokenCookie = response.getCookie(refreshTokenCookieName);

            assertThat(refreshTokenCookie).isNotNull();
            assertThat(refreshTokenCookie.getMaxAge()).isEqualTo(0);
            assertThat(refreshTokenRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName(
                "Should return OK response with access token and refresh token cookie " +
                        "when refresh request contains valid refresh token"
        )
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldReturnOkResponseWhenRefreshRequestContainsValidRefreshToken() throws Exception {
            var token = UUID.randomUUID().toString();

            var refreshToken = RefreshToken.builder()
                    .userId(1L)
                    .token(token)
                    .expirationTime(LocalDateTime.now().plusMinutes(1))
                    .build();

            var cookie = new Cookie(refreshTokenCookieName, token);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            refreshTokenRepository.save(refreshToken);
            assertThat(refreshTokenRepository.count()).isEqualTo(1);

            var response = mockMvc.perform(post("/api/auth/refresh")
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            var refreshTokenCookie = response.getCookie(refreshTokenCookieName);
            assertThat(refreshTokenCookie).isNotNull();
            assertThat(refreshTokenCookie.isHttpOnly()).isTrue();
            assertThat(refreshTokenCookie.getValue()).isNotEmpty();
            assertThat(refreshTokenCookie.getValue()).isNotEqualTo(token);

            var authentication = (AuthResponse) parseResponse(AuthResponse.class, response.getContentAsString());
            assertThat(authentication.getAccessToken()).isNotEmpty();
        }

        @Test
        @DisplayName("Should return FORBIDDEN response when refresh request contains expired refresh token")
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldReturn403ResponseWhenRefreshRequestContainsExpiredRefreshToken() throws Exception {
            var token = UUID.randomUUID().toString();

            var refreshToken = RefreshToken.builder()
                    .userId(1L)
                    .token(token)
                    .expirationTime(LocalDateTime.now().minusMinutes(1))
                    .build();

            var cookie = new Cookie(refreshTokenCookieName, token);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            refreshTokenRepository.save(refreshToken);
            assertThat(refreshTokenRepository.count()).isEqualTo(1);

            var response = mockMvc.perform(post("/api/auth/refresh")
                            .cookie(cookie))
                    .andExpect(status().isForbidden())
                    .andReturn()
                    .getResponse();

            assertThat(response.getContentAsString()).contains("Refresh token has expired");
        }

        @Test
        @DisplayName("Should return FORBIDDEN response when refresh request contains unknown refresh token")
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldReturn403ResponseWhenRefreshRequestContainsUnknownRefreshToken() throws Exception {
            var token = UUID.randomUUID().toString();

            var cookie = new Cookie(refreshTokenCookieName, token);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            var response = mockMvc.perform(post("/api/auth/refresh")
                            .cookie(cookie))
                    .andExpect(status().isForbidden())
                    .andReturn()
                    .getResponse();

            assertThat(response.getContentAsString()).contains("Token not found");
        }

        @Test
        @DisplayName("Should return FORBIDDEN response when refresh request has no refresh token cookie")
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldReturn403ResponseWhenRefreshRequestHasNoRefreshTokenCookie() throws Exception {
            var response = mockMvc.perform(post("/api/auth/refresh"))
                    .andExpect(status().isForbidden())
                    .andReturn()
                    .getResponse();

            assertThat(response.getContentAsString()).contains("Token is missing");
        }
    }
}
