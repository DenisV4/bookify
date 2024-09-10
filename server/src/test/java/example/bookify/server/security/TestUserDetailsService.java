package example.bookify.server.security;

import example.bookify.server.model.User;
import example.bookify.server.model.UserRole;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("test-user-details")
@Service
public class TestUserDetailsService implements UserDetailsService {

    private final User user = User.builder()
            .id(1L)
            .name("testUser")
            .email("test@me.com")
            .password("password")
            .roles(List.of(UserRole.builder().authority(UserRole.RoleType.ROLE_USER).build()))
            .build();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return AppUserDetails.builder().user(user).build();
    }
}
