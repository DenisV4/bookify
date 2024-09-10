package example.bookify.server.service;

import example.bookify.server.model.User;
import example.bookify.server.model.UserRole;
import example.bookify.server.web.dto.request.Pagination;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<User> findAll(Pagination pagination);

    User findById(Long id);

    User findByName(String name);

    User save(User user, List<UserRole> roles);

    User update(User user, List<UserRole> roles);

    void deleteById(Long id);

    Boolean existsByRoleType(UserRole.RoleType type);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);

    Boolean validateAdminRoles(User user, List<UserRole> roles);
}
