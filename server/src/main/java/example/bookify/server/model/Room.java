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
@Table(name = "rooms")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 4000, nullable = false)
    private String description;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private Float price;

    @Builder.Default
    @Column(name = "guests_number", nullable = false)
    private Integer guestsNumber = 1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = Booking.Fields.room , orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
}
