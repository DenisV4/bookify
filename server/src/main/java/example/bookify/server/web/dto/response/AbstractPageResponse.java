package example.bookify.server.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Getter
@Setter
public abstract class AbstractPageResponse {

    private Long totalElements;

    private Integer totalPages;
}
