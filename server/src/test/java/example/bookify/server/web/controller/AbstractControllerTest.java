package example.bookify.server.web.controller;

import com.redis.testcontainers.RedisContainer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AbstractControllerTest {

    @Container
    protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16")
    );

    @Container
    protected static RedisContainer REDIS_CONTAINER = new RedisContainer(
            DockerImageName.parse("redis:7.4.0")
    )
            .withExposedPorts(6379);

    @Container
    protected static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer(
            DockerImageName.parse("mongo:7.0.12")
    );

    @Container
    public static KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.7.0")
    );

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }
}
