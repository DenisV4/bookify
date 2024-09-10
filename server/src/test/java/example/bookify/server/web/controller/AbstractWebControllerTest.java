package example.bookify.server.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AbstractWebControllerTest extends AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    protected Object parseResponse(Class<?> clazz, String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, clazz);
        } catch (Exception exc) {
            throw new RuntimeException("Failed to parse JSON response", exc);
        }
    }
}
