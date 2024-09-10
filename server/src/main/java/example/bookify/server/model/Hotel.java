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
@Table(name = "hotels")
public class Hotel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Float distance;

    @Builder.Default
    @Column(nullable = false)
    private Float rating = 0F;

    @Builder.Default
    @Column(name = "ratings_count", nullable = false)
    private Integer ratingsCount = 0;

    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = Room.Fields.hotel, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();
}
