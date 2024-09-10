package example.bookify.server.web.dto.request.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;
import example.bookify.server.util.ToStringUtil;
import example.bookify.server.validator.annotation.CheckOutAfterCheckIn;
import example.bookify.server.validator.annotation.NullOrNotBlank;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.RequestWithCheckInAndCheckOutDates;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@CheckOutAfterCheckIn
@Schema(
        title = "Room filter",
        description = "Room filter parameters"
)
public class RoomFilter extends Pagination implements RequestWithCheckInAndCheckOutDates {

    @Positive(message = "'id' {positive.message}")
    private Long id;

    @NullOrNotBlank(message = "'name' {nullOrNotBlank.message}")
    private String name;

    @Positive(message = "'minPrice' {positive.message}")
    private Float minPrice;

    @Positive(message = "'maxPrice' {positive.message}")
    private Float maxPrice;

    @Positive(message = "'guestsNumber' {positive.message}")
    private Integer guestsNumber;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    @Positive(message = "'hotelId' {positive.message}")
    private Long hotelId;

    @NullOrNotBlank(message = "'city' {nullOrNotBlank.message}")
    private String city;

    @Override
    public String toString() {
        return ToStringUtil.generateToString(this);
    }
}
