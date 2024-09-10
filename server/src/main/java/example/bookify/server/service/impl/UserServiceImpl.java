package example.bookify.server.service.impl;

import lombok.RequiredArgsConstructor;
import example.bookify.server.exception.ResourceAlreadyExistsException;
import example.bookify.server.exception.ResourceNotFoundException;
import example.bookify.server.exception.UserRoleException;
import example.bookify.server.model.User;
import example.bookify.server.model.UserRole;
import example.bookify.server.repository.UserRepository;
import example.bookify.server.service.RefreshTokenService;
import example.bookify.server.service.UserService;
import example.bookify.server.util.BeanUtil;
import example.bookify.server.web.dto.request.Pagination;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Cacheable(value = "users", keyGenerator = "keyGenerator")
    public Page<User> findAll(Pagination pagination) {
        var pageRequest = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), Sort.by("id"));
        return userRepository.findAll(pageRequest);
    }

    @Override
    @Cacheable(value = "userById", keyGenerator = "keyGenerator")
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException.supply("User with id={0} was not found", id));
    }

    @Override
    @Cacheable(value = "userByName", keyGenerator = "keyGenerator")
    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(ResourceNotFoundException.supply("User with name '{0}' was not found", name));
    }

    @Transactional
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User save(User user, List<UserRole> roles) {
        checkUnique(user);
        var createdUser = userRepository.save(user);
        roles.forEach(role -> role.setUser(createdUser));
        createdUser.setRoles(roles);

        return userRepository.save(createdUser);
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = "userById", key = "#user.id"),
            },
            evict = {
                    @CacheEvict(value = "userByName", allEntries = true),
                    @CacheEvict(value = "users", allEntries = true),
                    @CacheEvict(value = "userExistsByName", allEntries = true),
                    @CacheEvict(value = "userExistsByEmail", allEntries = true)
            }
    )
    public User update(User user, List<UserRole> roles) {
        checkUnique(user);

        var existingUser = findById(user.getId());

        if (!validateAdminRoles(existingUser, roles)) {
            throw new UserRoleException("There will be no admin left after execution");
        }

        BeanUtil.copyNonNullProperties(user, existingUser);

        if (roles != null) {
            roles.forEach(role -> role.setUser(existingUser));
            existingUser.getRoles().clear();
            existingUser.getRoles().addAll(roles);
        }

        var updatedUser = userRepository.save(existingUser);
        refreshTokenService.deleteByUserId(updatedUser.getId());

        return updatedUser;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userById", allEntries = true),
            @CacheEvict(value = "userByName", allEntries = true),
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "userExistsByName", allEntries = true),
            @CacheEvict(value = "userExistsByEmail", allEntries = true)
    })
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "userExistsByName", keyGenerator = "keyGenerator")
    public Boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    @Override
    @Cacheable(value = "userExistsByEmail", keyGenerator = "keyGenerator")
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByRoleType(UserRole.RoleType type) {
        return userRepository.existsByRoles_Authority(type);
    }

    @Override
    public Boolean validateAdminRoles(User user, List<UserRole> roles) {
        if (roles != null && includesAdminRole(user.getRoles()) && !includesAdminRole(roles)) {
            return userRepository.countByRoles_Authority(UserRole.RoleType.ROLE_ADMIN) > 1;
        }
        return true;
    }

    private void checkUnique(User user) {
        User existingUser = null;
        if (user.getId() != null) {
            existingUser = findById(user.getId());
        }

        var name = user.getName();
        var email = user.getEmail();

        if (name != null) {
            if (existingUser != null && name.equals(existingUser.getName())) {
                return;
            }
            if (existsByName(name)) {
                throw new ResourceAlreadyExistsException(
                        "User with name `{0}` already exists", user.getName());
            }
        }

        if (email != null) {
            if (existingUser != null && email.equals(existingUser.getEmail())) {
                return;
            }
            if (existsByEmail(email)) {
                throw new ResourceAlreadyExistsException(
                        "User with email `{0}` already exists", user.getEmail());
            }
        }
    }

    private boolean includesAdminRole(List<UserRole> roles) {
        return roles.stream()
                .anyMatch(role -> role.getAuthority().equals(UserRole.RoleType.ROLE_ADMIN));
    }
}
