package com.property.tenantplatform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.property.tenantplatform.dto.ComplaintRequest;
import com.property.tenantplatform.exception.InvalidInputException;
import com.property.tenantplatform.exception.ResourceNotFoundException;
import com.property.tenantplatform.model.Complaint;
import com.property.tenantplatform.model.ComplaintStatus;
import com.property.tenantplatform.model.Property;
import com.property.tenantplatform.model.Tenant;
import com.property.tenantplatform.repository.ComplaintRepository;
import com.property.tenantplatform.repository.PropertyRepository;

@Service
public class ComplaintService {

  private final ComplaintRepository complaintRepository;
  private final PropertyRepository propertyRepository;
  private final TenantService tenantService;

  public ComplaintService(ComplaintRepository complaintRepository, PropertyRepository propertyRepository,
                          TenantService tenantService) {
    this.complaintRepository = complaintRepository;
    this.propertyRepository = propertyRepository;
    this.tenantService = tenantService;
  }

  @Transactional
  public Complaint create(Long userId, ComplaintRequest request) {
    Tenant tenant = tenantService.findByUserId(userId);
    Property property = propertyRepository.findById(request.getPropertyId())
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

    if (!tenant.getProperty().getId().equals(property.getId())) {
      throw new InvalidInputException("Tenant can only file complaints for assigned property");
    }

    Complaint complaint = new Complaint();
    complaint.setTenant(tenant);
    complaint.setProperty(property);
    complaint.setDescription(request.getDescription());
    complaint.setStatus(ComplaintStatus.OPEN);
    return complaintRepository.save(complaint);
  }

  @Transactional
  public Complaint updateStatus(Long complaintId, ComplaintStatus status) {
    Complaint complaint = complaintRepository.findById(complaintId)
        .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
    complaint.setStatus(status);
    return complaintRepository.save(complaint);
  }

  public List<Complaint> findAll() {
    return complaintRepository.findAll();
  }

  public List<Complaint> findForTenant(Long userId) {
    Tenant tenant = tenantService.findByUserId(userId);
    return complaintRepository.findAll().stream()
        .filter(c -> c.getTenant().getId().equals(tenant.getId()))
        .toList();
  }
}
