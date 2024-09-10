package example.bookify.server.web.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Hidden
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "OK";
    }
}
