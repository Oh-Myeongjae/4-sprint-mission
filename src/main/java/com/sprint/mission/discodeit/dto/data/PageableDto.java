package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.Min;
import java.util.List;

public record PageableDto(
    @Min(1)
    Integer size,

    @Min(0)
    Integer page,

    List<String> sort
) {

}
