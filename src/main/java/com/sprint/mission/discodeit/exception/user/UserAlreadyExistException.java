package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException(ErrorCode errorCode, UUID userId) {
    super(errorCode, Map.of("already exist userId", userId));
  }
}