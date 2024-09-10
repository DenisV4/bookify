package example.bookify.server.web.controller;

import example.bookify.server.model.User;
import example.bookify.server.service.UserService;
import example.bookify.server.web.dto.request.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@SuppressWarnings("unchecked")
@DisplayName("UserController Security Tests")
class UserControllerSecurityTests extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private final String USER_JSON = """
                {
                    "name": "test",
                    "email": "test@me.com",
                    "password": "test",
                    "roles": ["ROLE_USER"]
                }
            """;

    @Test
    @DisplayName("Should return OK and allow access to GET /api/users for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetAllUsers() throws Exception {
        when(userService.findAll(any(Pagination.class)))
                .thenReturn(any(Page.class));

        mvc.perform(get("/api/users")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/users for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetAllUsersForUser() throws Exception {
        when(userService.findAll(any(Pagination.class)))
                .thenReturn(any(Page.class));

        mvc.perform(get("/api/users")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/users for unauthorized users")
    void shouldNotAllowAccessToGetAllUsers() throws Exception {
        mvc.perform(get("/api/users")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/users/{id} for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetUserById() throws Exception {
        when(userService.findById(1L))
                .thenReturn(new User());

        mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/users/{id} for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetUserByIdForUser() throws Exception {
        when(userService.findById(1L))
                .thenReturn(new User());

        mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/users/{id} for unauthorized users")
    void shouldNotAllowAccessToGetUserById() throws Exception {
        mvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return CREATED and allow access to POST /api/users for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToCreateUser() throws Exception {
        when(userService.save(any(User.class), any(List.class)))
                .thenReturn(new User());

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to POST /api/users for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToCreateUserForUser() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to POST /api/users for unauthorized users")
    void shouldNotAllowAccessToCreateUser() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to PUT /api/users for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToUpdateUser() throws Exception {
        when(userService.update(any(User.class), any(List.class)))
                .thenReturn(new User());

        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isOk());
    }

    @Nested
    @ActiveProfiles({"test", "test-user-details"})
    @DisplayName("Update User by USER Security Tests")
    class UpdateUserTests {

        @Test
        @DisplayName("Should return OK and allow access to PUT /api/users without new roles for target USER")
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldAllowAccessToUpdateUserWithoutRolesForTargetUser() throws Exception {
            var userJson = """
                    {
                        "name": "testUser",
                        "email": "testUser@me.com"
                    }
                """;
            when(userService.update(any(User.class), any(List.class)))
                    .thenReturn(new User());

            mvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return FORBIDDEN and forbid access to PUT /api/users with new roles for target USER")
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldNotAllowAccessToUpdateUserWithRolesForTargetUser() throws Exception {
            mvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(USER_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return FORBIDDEN and forbid access to PUT /api/users for another USER")
        @WithUserDetails(
                userDetailsServiceBeanName = "testUserDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void shouldNotAllowAccessToUpdateUserForAnotherUser() throws Exception {
            mvc.perform(put("/api/users/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(USER_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to PUT /api/users for unauthorized users")
    void shouldNotAllowAccessToUpdateUserForAnonymous() throws Exception {
        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return NOCONTENT and allow access to DELETE /api/users for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToDeleteUser() throws Exception {
        doNothing().when(userService).deleteById(anyLong());

        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to DELETE /api/users for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToDeleteUserForUser() throws Exception {
        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to DELETE /api/users for unauthorized users")
    void shouldNotAllowAccessToDeleteUser() throws Exception {
        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }
}
