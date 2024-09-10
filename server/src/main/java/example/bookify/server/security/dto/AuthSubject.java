package example.bookify.server.security.dto;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSubject {

    private Long id;

    private String name;

    private String email;

    private List<String> roles;
}
