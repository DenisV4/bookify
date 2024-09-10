package example.bookify.server.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "Bearer AuthenticationStat",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "JWT Authorization header using the Bearer scheme."
)
public class OpenApiConfig {

    @Value("${app.openapi.demo-server-url}")
    String demoServerUrl;

    @Bean
    public OpenAPI openApi() {
        var demoServer = new Server();
        demoServer.setUrl(demoServerUrl);
        demoServer.setDescription("Demo server");

        var devServer = new Server();
        devServer.setUrl("https://localhost:8090");
        devServer.setDescription("Development server");

        var contact = new Contact();
        contact.setName("Gojko Mitiс");
        contact.setEmail("g.mitic@bookify.example");
        contact.setUrl("https://bookify.example");

        var gplv3License = new License()
                .name("GUN GPLv3")
                .url("https://choosealicense.com/licenses/gpl-3.0/");

        var info = new Info()
                .title("Bookify reservation service API")
                .version("1.0")
                .contact(contact)
                .description("API for performing operations with hotel reservation")
                .termsOfService("https://bookify.example/terms")
                .license(gplv3License);

        return new OpenAPI()
                .info(info)
                .servers(List.of(demoServer, devServer));
    }
}
