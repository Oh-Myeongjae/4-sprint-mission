package com.sprint.mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
    log.warn("IllegalArgumentException occurred: {}", exception.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(exception,ErrorCode.INVALID_ARGUMENT);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException exception) {
    log.warn("NoSuchElementException occurred: {}", exception.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(exception,ErrorCode.ELEMENT_NOT_FOUND);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  // ValidationException 처리 추가
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
    Map<String, Object> validationErrors = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });

    log.warn("Validation failed: {}", validationErrors);
    ErrorResponse errorResponse = new ErrorResponse(exception,ErrorCode.VALIDATION_FAILED,validationErrors);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    log.error("DiscodeitException occurred - Code: {}, Message: {}, Details: {}",
        errorCode.name(), exception.getMessage(), exception.getDetails());

    ErrorResponse errorResponse = new ErrorResponse(exception, errorCode.getStatus());
    return new ResponseEntity<>(errorResponse, errorCode.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
    log.error("Unexpected exception occurred: {}", exception.getMessage(), exception);
    ErrorResponse errorResponse = new ErrorResponse(exception, ErrorCode.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
