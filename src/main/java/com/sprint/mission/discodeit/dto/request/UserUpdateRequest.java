package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
    @NotBlank(message = "새로운 사용자명은 필수입니다")
    String newUsername,

    @NotBlank(message = "새로운 이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이 아닙니다")
    String newEmail,

    @NotBlank(message = "새로운 비밀번호는 필수입니다")
    String newPassword
) {

}