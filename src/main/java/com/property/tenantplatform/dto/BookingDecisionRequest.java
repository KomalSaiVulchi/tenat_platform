package com.property.tenantplatform.dto;

import com.property.tenantplatform.model.BookingStatus;

import jakarta.validation.constraints.NotNull;

public class BookingDecisionRequest {
  @NotNull
  private BookingStatus status;

  public BookingDecisionRequest() {
  }

  public BookingStatus getStatus() {
    return status;
  }

  public void setStatus(BookingStatus status) {
    this.status = status;
  }
}
