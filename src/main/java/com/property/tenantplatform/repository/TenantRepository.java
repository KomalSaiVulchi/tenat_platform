package com.property.tenantplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.tenantplatform.model.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
  boolean existsByEmail(String email);
  Optional<Tenant> findByUserId(Long userId);
}
