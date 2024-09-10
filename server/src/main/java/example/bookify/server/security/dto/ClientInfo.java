package example.bookify.server.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ClientInfo {

    private String userAgent;

    private String remoteAddress;
}
