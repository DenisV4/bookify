package example.bookify.server.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationEvent {

    private Long userId;

    private String name;

    private String email;
}
