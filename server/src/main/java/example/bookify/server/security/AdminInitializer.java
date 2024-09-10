package example.bookify.server.security;

import example.bookify.server.model.User;
import example.bookify.server.model.UserRole;
import example.bookify.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Profile("!test")
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.default-admin.username:admin}")
    private String username;

    @Value("${app.security.default-admin.email:admin@example.ru}")
    private String email;

    @Value("${app.security.default-admin.password:admin}")
    private String password;

    @EventListener(ContextRefreshedEvent.class)
    public void createDefaultAdmin() {
        var hasUserWithAdminRole = userService.existsByRoleType(UserRole.RoleType.ROLE_ADMIN);
        if (hasUserWithAdminRole) {
            return;
        }

        var admin = User.builder()
                .name(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        var adminRole = UserRole.builder()
                .authority(UserRole.RoleType.ROLE_ADMIN)
                .build();
        var roles = new ArrayList<>(List.of(adminRole));

        userService.save(admin, roles);

        log.info("Default admin created with credentials: username: {}, password: {}", admin.getName(), password);
    }
}
