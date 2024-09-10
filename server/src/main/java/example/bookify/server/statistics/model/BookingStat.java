package example.bookify.server.statistics.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("booking_stats")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingStat {

    @Id
    @CsvIgnore
    private String id;

    @CsvBindByName(column = "user_id")
    private Long userId;

    @CsvBindByName(column = "room_id")
    private Long roomId;

    @CsvBindByName(column = "check_in_date")
    private LocalDate checkInDate;

    @CsvBindByName(column = "check_out_date")
    private LocalDate checkOutDate;

    @CreatedDate
    @CsvBindByName(column = "time")
    private LocalDateTime timestamp;
}
