package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일명은 필수입니다")
    String fileName,

    @NotBlank(message = "콘텐츠 타입은 필수입니다")
    String contentType,

    @Size(min = 1, message = "파일 데이터는 비어있을 수 없습니다")
    byte[] bytes
) {

}
