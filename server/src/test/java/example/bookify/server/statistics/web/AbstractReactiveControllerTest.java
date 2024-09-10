package example.bookify.server.statistics.web;

import example.bookify.server.web.controller.AbstractControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles({"test", "no-security"})
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class AbstractReactiveControllerTest extends AbstractControllerTest {

    @Autowired
    protected WebTestClient webTestClient;

    @MockBean
    protected AuthenticationManager authenticationManager;

    @MockBean
    protected PasswordEncoder passwordEncoder;
}
