package example.bookify.server.repository;

import example.bookify.server.model.User;
import example.bookify.server.model.UserRole;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @EntityGraph(attributePaths = User.Fields.roles)
    @NonNull List<User> findAll();

    @Override
    @EntityGraph(attributePaths = User.Fields.roles)
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);

    @Override
    @EntityGraph(attributePaths = User.Fields.roles)
    @NonNull Optional<User> findById(@NonNull Long id);

    @EntityGraph(attributePaths = User.Fields.roles)
    Optional<User> findByName(String name);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);

    Boolean existsByRoles_Authority(UserRole.RoleType authority);

    Integer countByRoles_Authority(UserRole.RoleType authority);
}
