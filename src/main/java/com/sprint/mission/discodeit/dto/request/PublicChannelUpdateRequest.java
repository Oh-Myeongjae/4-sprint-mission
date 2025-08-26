package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelUpdateRequest(
    @NotBlank(message = "새로운 채널명은 필수입니다")
    String newName,

    String newDescription
) {

}