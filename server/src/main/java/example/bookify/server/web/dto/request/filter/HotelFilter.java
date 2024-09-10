package example.bookify.server.web.dto.request.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;
import example.bookify.server.util.ToStringUtil;
import example.bookify.server.validator.annotation.NullOrNotBlank;
import example.bookify.server.web.dto.request.Pagination;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jboss.logging.Messages;

import java.text.MessageFormat;
import java.util.StringJoiner;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
        title = "Hotel filter",
        description = "Hotel filter parameters"
)
public class HotelFilter extends Pagination {

    @Positive(message = "'id' {positive.message}")
    private Long id;

    @NullOrNotBlank(message = "'name' {nullOrNotBlank.message}")
    private String name;

    @NullOrNotBlank(message = "'title' {nullOrNotBlank.message}")
    private String title;

    @NullOrNotBlank(message = "'city' {nullOrNotBlank.message}")
    private String city;

    @NullOrNotBlank(message = "'address' {nullOrNotBlank.message}")
    private String address;

    @Positive(message = "'distance' {positive.message}")
    private Float distance;

    @Positive(message = "'rating' {positive.message}")
    private Float rating;

    @Positive(message = "'ratingsCount' {positive.message}")
    private Integer ratingsCount;

    @Override
    public String toString() {
        return ToStringUtil.generateToString(this);
    }
}
