package com.property.tenantplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.tenantplatform.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}
