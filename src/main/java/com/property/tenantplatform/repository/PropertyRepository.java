package com.property.tenantplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.tenantplatform.model.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
