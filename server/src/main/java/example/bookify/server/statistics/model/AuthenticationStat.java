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

@Document("authentication")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationStat {

    @Id
    @CsvIgnore
    private String id;

    @CsvBindByName(column = "user_id", required = true)
    private Long userId;

    @CsvBindByName(column = "name", required = true)
    private String name;

    @CsvBindByName(column = "device", required = true)
    private String device;

    @CsvBindByName(column = "os", required = true)
    private String os;

    @CsvBindByName(column = "browser", required = true)
    private String browser;

    @CsvBindByName(column = "remote_address", required = true)
    private String remoteAddress;

    @CreatedDate
    @CsvBindByName(column = "time")
    private LocalDateTime timestamp;
}
