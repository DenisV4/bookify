package example.bookify.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
@EnableReactiveMongoRepositories("example.bookify.server.statistics.repository")
@EnableReactiveMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }
}
