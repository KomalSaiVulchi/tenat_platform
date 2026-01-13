package com.property.tenantplatform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.tenantplatform.dto.TenantRequest;
import com.property.tenantplatform.model.Tenant;
import com.property.tenantplatform.service.CurrentUserService;
import com.property.tenantplatform.service.TenantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

  private final TenantService tenantService;
  private final CurrentUserService currentUserService;

  public TenantController(TenantService tenantService, CurrentUserService currentUserService) {
    this.tenantService = tenantService;
    this.currentUserService = currentUserService;
  }

  @GetMapping
  public ResponseEntity<List<Tenant>> list() {
    return ResponseEntity.ok(tenantService.findAll());
  }

  @GetMapping("/me")
  public ResponseEntity<Tenant> me(Authentication authentication) {
    Long userId = currentUserService.getCurrentUserId(authentication);
    return ResponseEntity.ok(tenantService.findByUserId(userId));
  }

  @PostMapping
  public ResponseEntity<Tenant> create(@Valid @RequestBody TenantRequest request) {
    return ResponseEntity.ok(tenantService.create(request));
  }
}
