package com.property.tenantplatform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.tenantplatform.dto.BookingDecisionRequest;
import com.property.tenantplatform.dto.BookingRequest;
import com.property.tenantplatform.model.Booking;
import com.property.tenantplatform.service.BookingService;
import com.property.tenantplatform.service.CurrentUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

  private final BookingService bookingService;
  private final CurrentUserService currentUserService;

  public BookingController(BookingService bookingService, CurrentUserService currentUserService) {
    this.bookingService = bookingService;
    this.currentUserService = currentUserService;
  }

  @PostMapping
  public ResponseEntity<Booking> create(Authentication authentication,
                                        @Valid @RequestBody BookingRequest request) {
    Long userId = currentUserService.getCurrentUserId(authentication);
    return ResponseEntity.ok(bookingService.create(userId, request));
  }

  @PostMapping("/{id}/decide")
  public ResponseEntity<Booking> decide(@PathVariable Long id,
                                        @Valid @RequestBody BookingDecisionRequest request) {
    return ResponseEntity.ok(bookingService.decide(id, request));
  }

  @GetMapping
  public ResponseEntity<List<Booking>> list() {
    return ResponseEntity.ok(bookingService.findAll());
  }

  @GetMapping("/me")
  public ResponseEntity<List<Booking>> myBookings(Authentication authentication) {
    Long userId = currentUserService.getCurrentUserId(authentication);
    return ResponseEntity.ok(bookingService.findForTenant(userId));
  }
}
