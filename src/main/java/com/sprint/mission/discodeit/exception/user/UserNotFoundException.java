package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(ErrorCode errorCode, UUID userId) {
    super(errorCode, Map.of("not found userId", userId));
  }
}