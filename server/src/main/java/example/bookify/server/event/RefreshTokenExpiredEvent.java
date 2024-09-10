package example.bookify.server.event;

import lombok.extern.slf4j.Slf4j;
import example.bookify.server.model.RefreshToken;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RefreshTokenExpiredEvent {

    @EventListener
    public void handleRefreshTokenExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        var refreshToken = (RefreshToken) event.getValue();

        if (refreshToken == null) {
            log.error("Refresh token is null");
            return;
        }

        log.info("Refresh token with key={} has expired. Refresh token: {}",
                refreshToken.getId(),
                refreshToken.getToken());
    }
}
