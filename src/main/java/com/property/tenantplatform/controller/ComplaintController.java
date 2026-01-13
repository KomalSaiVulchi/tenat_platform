package com.property.tenantplatform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.property.tenantplatform.dto.ComplaintRequest;
import com.property.tenantplatform.model.Complaint;
import com.property.tenantplatform.model.ComplaintStatus;
import com.property.tenantplatform.service.ComplaintService;
import com.property.tenantplatform.service.CurrentUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

  private final ComplaintService complaintService;
  private final CurrentUserService currentUserService;

  public ComplaintController(ComplaintService complaintService, CurrentUserService currentUserService) {
    this.complaintService = complaintService;
    this.currentUserService = currentUserService;
  }

  @PostMapping
  public ResponseEntity<Complaint> create(Authentication authentication,
                                          @Valid @RequestBody ComplaintRequest request) {
    Long userId = currentUserService.getCurrentUserId(authentication);
    return ResponseEntity.ok(complaintService.create(userId, request));
  }

  @PostMapping("/{id}/status")
  public ResponseEntity<Complaint> updateStatus(@PathVariable Long id,
                                                @RequestParam ComplaintStatus status) {
    return ResponseEntity.ok(complaintService.updateStatus(id, status));
  }

  @GetMapping
  public ResponseEntity<List<Complaint>> list() {
    return ResponseEntity.ok(complaintService.findAll());
  }

  @GetMapping("/me")
  public ResponseEntity<List<Complaint>> myComplaints(Authentication authentication) {
    Long userId = currentUserService.getCurrentUserId(authentication);
    return ResponseEntity.ok(complaintService.findForTenant(userId));
  }
}
