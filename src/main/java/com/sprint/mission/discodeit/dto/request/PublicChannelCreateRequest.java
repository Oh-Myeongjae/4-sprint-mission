package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널명은 필수입니다")
    @Size(min = 2)
    String name,

    @Size(max = 255)
    String description
) {

}