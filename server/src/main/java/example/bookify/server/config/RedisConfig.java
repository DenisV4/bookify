package example.bookify.server.config;

import lombok.NonNull;
import example.bookify.server.model.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableRedisRepositories(
        keyspaceConfiguration = RedisConfig.RefreshTokenKeyspaceConfiguration.class,
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
public class RedisConfig {

    @Value("${app.security.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());

        return new JedisConnectionFactory(configuration);
    }

    public class RefreshTokenKeyspaceConfiguration extends KeyspaceConfiguration {

        private static final String REFRESH_TOKEN_KEYSPACE = "refresh_tokens";

        @Override
        @NonNull
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            var keyspaceSettings = new KeyspaceSettings(RefreshToken.class, REFRESH_TOKEN_KEYSPACE);
            keyspaceSettings.setTimeToLive(refreshTokenExpiration.getSeconds());

            return Collections.singleton(keyspaceSettings);
        }
    }

    @Bean
    @Profile("test")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());

        return template;
    }
}
