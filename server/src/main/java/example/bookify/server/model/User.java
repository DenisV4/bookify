package example.bookify.server.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Builder.Default
    @OneToMany(
            mappedBy = UserRole.Fields.user, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true
    )
    private List<UserRole> roles = new ArrayList<>();

    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = Booking.Fields.user , orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
}
