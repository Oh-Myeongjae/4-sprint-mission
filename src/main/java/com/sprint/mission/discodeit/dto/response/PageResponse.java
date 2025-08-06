package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    Integer number,
    Integer size,
    boolean hasNext,
    Long totalElements
) {

}
