package com.property.tenantplatform.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiError {
  private final LocalDateTime timestamp = LocalDateTime.now();
  private final int status;
  private final String error;
  private final String message;
  private final String path;

  public ApiError(HttpStatus status, String message, String path) {
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.message = message;
    this.path = path;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public String getPath() {
    return path;
  }
}
