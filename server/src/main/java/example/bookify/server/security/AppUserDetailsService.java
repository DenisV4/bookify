package example.bookify.server.security;

import example.bookify.server.exception.ResourceNotFoundException;
import example.bookify.server.model.User;
import example.bookify.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Profile("!test-user-details")
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        try {
            user = userService.findByName(username);
        } catch (ResourceNotFoundException exc) {
            throw new UsernameNotFoundException(exc.getMessage());
        }

        return AppUserDetails.builder()
                .user(user)
                .build();
    }
}
