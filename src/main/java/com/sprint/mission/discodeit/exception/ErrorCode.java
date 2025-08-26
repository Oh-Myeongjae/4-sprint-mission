package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // User 관련 에러
  USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_ALREADY_EXISTS("이미 존재하는 사용자입니다.", HttpStatus.CONFLICT),
  INVALID_CREDENTIALS("사용자 정보와 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

  // Channel 관련 에러 추가
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED("프라이빗 채널은 수정할 수 없습니다.", HttpStatus.FORBIDDEN),

  // BinaryContent 관련 에러
  BINARY_CONTENT_NOT_FOUND("바이너리 콘텐츠를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // Message 관련 에러
  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // ReadStatus 관련 에러
  READSTATUS_ALREADY_EXISTS("읽기 상태가 이미 존재합니다.", HttpStatus.CONFLICT),
  READSTATUS_NOT_FOUND("읽기 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // UserStatus 관련 에러
  USERSTATUS_ALREADY_EXISTS("사용자 상태가 이미 존재합니다.", HttpStatus.CONFLICT),
  USERSTATUS_NOT_FOUND("사용자 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // 일반적인 에러들
  INVALID_ARGUMENT("잘못된 인자입니다.", HttpStatus.BAD_REQUEST),
  ELEMENT_NOT_FOUND("요청한 요소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  VALIDATION_FAILED("입력값 검증에 실패했습니다.", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  private String message;
  private HttpStatus status;

  ErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}