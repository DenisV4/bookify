package example.bookify.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@RedisHash("refresh_tokens")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Indexed
    private Long id;

    @Indexed
    private Long userId;

    @Indexed
    private String token;

    @Indexed
    private LocalDateTime expirationTime;
}
