package example.bookify.server.service.impl;

import com.redis.testcontainers.RedisContainer;
import example.bookify.server.config.CacheConfig;
import example.bookify.server.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(classes = {
        CacheConfig.class,
        RedisConfig.class,
        RedisAutoConfiguration.class,
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AbstractServiceCacheTest {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Container
    protected static final RedisContainer REDIS_CONTAINER = new RedisContainer(
            DockerImageName.parse("redis:7.4.0")
    )
            .withExposedPorts(6379);

    @DynamicPropertySource
    protected static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @BeforeEach
    public void init() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection().serverCommands().flushAll();
    }

    protected static <T> Page<T> createPage(int number, int size, List<T> content) {
        return new PageImpl<>(
                content,
                PageRequest.of(number, size, Sort.by("id")),
                content.size()
        );
    }

    protected void populateCache(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    protected Object getCacheValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    protected void assertCacheKeyCount(int expectedCount) {
        assertThat(redisTemplate.keys("*")).hasSize(expectedCount);
    }

    protected void assertCacheContainsKey(String key) {
        assertThat(redisTemplate.keys(key)).isNotEmpty();
    }

    protected void assertCacheDoesNotContainKey(String key) {
        assertThat(redisTemplate.keys(key)).isEmpty();
    }

    protected void assertObjectsEqual(Object obj1, Object obj2) {
       assertThat(obj1).usingRecursiveComparison().isEqualTo(obj2);
    }

    protected void assertPagesEqual(Page<?> result1, Page<?> result2) {
        assertThat(result1.getTotalElements()).isEqualTo(result2.getTotalElements());
        assertThat(result1.getTotalPages()).isEqualTo(result2.getTotalPages());
        assertThat(result1.getContent()).usingRecursiveFieldByFieldElementComparator().isEqualTo(result2.getContent());
    }
}
