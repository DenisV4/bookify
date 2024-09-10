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

import java.time.LocalDateTime;

@Document("user_registrations")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistration {

    @Id
    @CsvIgnore
    private String id;

    @CsvBindByName(column = "user_id", required = true)
    private Long userId;

    @CsvBindByName(column = "name", required = true)
    private String name;

    @CsvBindByName(column = "email", required = true)
    private String email;

    @CreatedDate
    @CsvBindByName(column = "time")
    private LocalDateTime timestamp;
}
