package example.bookify.server.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Pagination {

    @NotNull(message = "'pageNumber' {notNull.message}")
    @PositiveOrZero(message = "pageNumber' {positiveOrZero.message}")
    private Integer pageNumber;

    @NotNull(message = "'pageSize' {notNull.message}")
    @Positive(message = "'pageSize' {positive.message}")
    private Integer pageSize;
}
