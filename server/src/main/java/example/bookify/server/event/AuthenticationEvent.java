package example.bookify.server.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationEvent {

    private Long userId;

    private String name;

    private String device;

    private String os;

    private String browser;

    private String remoteAddress;
}
