package example.bookify.server.service.impl;

import example.bookify.server.exception.UserRoleException;
import example.bookify.server.model.User;
import example.bookify.server.model.UserRole;
import example.bookify.server.repository.UserRepository;
import example.bookify.server.service.RefreshTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("UserServiceImpl Unit Tests")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("Should update user successfully when at least one admin remains")
    void shouldUpdateUserWhenAtLeastOneAdminRoleRemains() {
        var userToUpdate = User.builder().id(1L).build();
        var existingUser = User.builder()
                .id(1L)
                .roles(List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_ADMIN).build()))
                .build();
        var newRoles = List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_USER).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.countByRoles_Authority(UserRole.RoleType.ROLE_ADMIN)).thenReturn(2);
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        var updatedUser = userService.update(userToUpdate, newRoles);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getRoles()).containsExactlyElementsOf(newRoles);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should throw UserRoleException when no admins remain after update")
    void shouldThrowUserRoleExceptionWhenNoAdminRolesRemain() {
        var userToUpdate = User.builder().id(1L).build();
        var existingUser = User.builder()
                .id(1L)
                .roles(List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_ADMIN).build()))
                .build();
        var newRoles = List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_USER).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.countByRoles_Authority(UserRole.RoleType.ROLE_ADMIN)).thenReturn(1);

        assertThatThrownBy(() -> userService.update(userToUpdate, newRoles))
                .isInstanceOf(UserRoleException.class)
                .hasMessageContaining("There will be no admin left after execution");
    }

    @Test
    @DisplayName("Should update user with new roles when user has existing roles")
    void shouldUpdateUserWithNewRolesWhenUserHasExistingRoles() {
        var userToUpdate = User.builder().id(1L).build();
        var existingUser = User.builder()
                .id(1L)
                .roles(List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_USER).build()))
                .build();
        var newRoles = List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_ADMIN).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        var updatedUser = userService.update(userToUpdate, newRoles);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getRoles()).containsExactlyElementsOf(newRoles);
        verify(userRepository).save(existingUser);
    }
}
