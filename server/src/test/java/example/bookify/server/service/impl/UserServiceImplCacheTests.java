package example.bookify.server.service.impl;

import example.bookify.server.model.User;
import example.bookify.server.repository.UserRepository;
import example.bookify.server.service.RefreshTokenService;
import example.bookify.server.service.UserService;
import example.bookify.server.web.dto.request.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("UserService Cache Tests")
@ContextConfiguration(classes = {
        UserService.class,
        UserServiceImpl.class,
})
class UserServiceImplCacheTests extends AbstractServiceCacheTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("Should cache the results of the findAll method")
    void shouldCacheResultsOfFindAllMethod() {
        var pagination = new Pagination();
        pagination.setPageNumber(0);
        pagination.setPageSize(10);

        var user1 = User.builder().id(1L).build();
        var user2 = User.builder().id(2L).build();
        var page = createPage(0, 10, List.of(user1, user2));

        when(userRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(page);

        assertCacheKeyCount(0);

        var result1 = userService.findAll(pagination);
        var result2 = userService.findAll(pagination);

        assertCacheContainsKey("users::Pagination(pageNumber=0, pageSize=10)");
        assertPagesEqual(result1, result2);

        verify(userRepository, times(1))
                .findAll(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    @DisplayName("Should cache the result of findById method")
    void shouldCacheResultOfFindByIdMethod() {
        var user = User.builder().id(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertCacheKeyCount(0);

        var result1 = userService.findById(1L);
        var result2 = userService.findById(1L);

        assertCacheContainsKey("userById::1");
        assertObjectsEqual(result1, result2);

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should cache the result of findByName method")
    void shouldCacheResultOfFindByNameMethod() {
        var userName = "testUser";
        var user = User.builder().id(1L).name(userName).build();

        when(userRepository.findByName(userName)).thenReturn(Optional.of(user));

        assertCacheKeyCount(0);

        var result1 = userService.findByName(userName);
        var result2 = userService.findByName(userName);

        assertCacheContainsKey("userByName::testUser");
        assertObjectsEqual(result1, result2);

        verify(userRepository, times(1)).findByName(userName);
    }

    @Test
    @DisplayName("Should evict 'users' cache when saving a new User")
    void shouldUpdateCacheWhenSavingNewUser() {
        var user = User.builder().build();
        var existingUsers = createPage(0, 10, List.of(user));

        when(userRepository.save(user)).thenReturn(user);

        assertCacheKeyCount(0);

        populateCache("users::Pagination(pageNumber=0, pageSize=10)", existingUsers);

        assertCacheKeyCount(1);

        userService.save(user, new ArrayList<>());

        assertCacheDoesNotContainKey("users::*");
    }

    @Test
    @DisplayName(
            "Should update 'userById' cache and  evict 'users' 'userByName'," +
                    " 'userExistsByName' and 'userExistsByEmail' caches when updating an existing User"
    )
    void shouldUpdateCacheWhenUpdatingExistingUser() {
        var user = User.builder().id(1L).name("updatedUser").build();
        var existingUser = User.builder().id(1L).name("existingUser").build();
        var existingUsers = createPage(0, 10, List.of(existingUser));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        assertCacheKeyCount(0);

        populateCache("users::Pagination(pageNumber=0, pageSize=10)", existingUsers);
        populateCache("userById::1", existingUser);
        populateCache("userByName::existingUser", existingUser);
        populateCache("userExistsByName::existingUser@example.com", true);
        populateCache("userExistsByEmail::existingUser@example.com", true);

        assertCacheKeyCount(5);

        userService.update(user, new ArrayList<>());

        var cachedUser = getCacheValue("userById::1");
        assertObjectsEqual(cachedUser, user);
        assertCacheDoesNotContainKey("users::*");
        assertCacheDoesNotContainKey("userByName::*");
        assertCacheDoesNotContainKey("userExistsByName::*");
        assertCacheDoesNotContainKey("userExistsByEmail::*");
    }

    @Test
    @DisplayName("Should evict all caches when deleting a User")
    void shouldEvictCacheWhenDeletingUser() {
        var userId = 1L;
        var existingUser = User.builder().id(1L).name("existingUser").build();
        var existingUsers = createPage(0, 10, List.of(existingUser));

        doNothing().when(userRepository).deleteById(userId);

        assertCacheKeyCount(0);

        populateCache("users::Pagination(pageNumber=0, pageSize=10)", existingUsers);
        populateCache("userById::1", existingUser);
        populateCache("userByName::existingUser", existingUser);
        populateCache("userExistsByName::existingUser@example.com", true);
        populateCache("userExistsByEmail::existingUser@example.com", true);

        assertCacheKeyCount(5);

        userService.deleteById(userId);

        assertCacheDoesNotContainKey("users::*");
        assertCacheDoesNotContainKey("userById::*");
        assertCacheDoesNotContainKey("userByName::*");
        assertCacheDoesNotContainKey("userExistsByName::*");
        assertCacheDoesNotContainKey("userExistsByEmail::*");
    }

    @Test
    @DisplayName("Should cache the result of existsByName method")
    void shouldCacheResultOfExistsByNameMethod() {
        var userName = "testUser";
        when(userRepository.existsByName(userName)).thenReturn(true);

        assertCacheKeyCount(0);

        var result1 = userService.existsByName(userName);
        var result2 = userService.existsByName(userName);

        assertCacheContainsKey("userExistsByName::testUser");
        assertObjectsEqual(result1, result2);

        verify(userRepository, times(1)).existsByName(userName);
    }

    @Test
    @DisplayName("Should cache the result of existsByEmail method")
    void shouldCacheResultOfExistsByEmailMethod() {
        var email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertCacheKeyCount(0);

        var result1 = userService.existsByEmail(email);
        var result2 = userService.existsByEmail(email);

        assertCacheContainsKey("userExistsByEmail::test@example.com");
        assertObjectsEqual(result1, result2);

        verify(userRepository, times(1)).existsByEmail(email);
    }
}
