package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

  private Instant timestamp;
  private String code;
  private String message;
  private Map<String, Object> details;
  private String exceptionType;
  private int status;

  public ErrorResponse(Exception exception, ErrorCode errorCode){
    this.timestamp = Instant.now();
    this.code = errorCode.name();
    this.message = exception.getMessage();
    this.exceptionType = exception.getClass().getName();
    this.status = errorCode.getStatus().value();
  }

  public ErrorResponse(Exception exception, ErrorCode errorCode, Map<String, Object> details){
    this.timestamp = Instant.now();
    this.code = errorCode.name();
    this.message = exception.getMessage();
    this.details = details;
    this.exceptionType = exception.getClass().getName();
    this.status = errorCode.getStatus().value();
  }

  public ErrorResponse (DiscodeitException exception, HttpStatus status) {
        this.timestamp = exception.getTimestamp();
        this.code = exception.getErrorCode().name();
        this.message = exception.getMessage();
        this.details = exception.getDetails();
        this.exceptionType = exception.getClass().getName();
        this.status = status.value();
  }
}