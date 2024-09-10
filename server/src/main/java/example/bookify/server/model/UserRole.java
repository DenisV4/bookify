package example.bookify.server.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@Entity
@Table(name = "user_roles")
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType authority;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(getAuthority().name());
    }

    public enum RoleType {
        ROLE_ADMIN,
        ROLE_USER
    }
}
