package example.bookify.server.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topic.statistics.authentication}")
    private String authenticationTopic;

    @Value("${app.kafka.topic.statistics.user-registration}")
    private String userRegistrationTopic;

    @Value("${app.kafka.topic.statistics.booking}")
    private String bookingTopic;

    @Bean
    NewTopic topic1() {
        return new NewTopic(authenticationTopic, 1, (short) 1);
    }

    @Bean
    NewTopic topic2() {
        return new NewTopic(userRegistrationTopic, 1, (short) 1);
    }

    @Bean
    NewTopic topic3() {
        return new NewTopic(bookingTopic, 1, (short) 1);
    }
}
