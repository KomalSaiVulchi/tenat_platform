package com.property.tenantplatform.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.property.tenantplatform.dto.TenantRequest;
import com.property.tenantplatform.exception.InvalidInputException;
import com.property.tenantplatform.exception.ResourceNotFoundException;
import com.property.tenantplatform.model.Property;
import com.property.tenantplatform.model.Tenant;
import com.property.tenantplatform.model.User;
import com.property.tenantplatform.repository.PropertyRepository;
import com.property.tenantplatform.repository.TenantRepository;
import com.property.tenantplatform.repository.UserRepository;

@Service
public class TenantService {

  private final TenantRepository tenantRepository;
  private final PropertyRepository propertyRepository;
  private final UserRepository userRepository;

  public TenantService(TenantRepository tenantRepository, PropertyRepository propertyRepository,
                       UserRepository userRepository) {
    this.tenantRepository = tenantRepository;
    this.propertyRepository = propertyRepository;
    this.userRepository = userRepository;
  }

  public List<Tenant> findAll() {
    return tenantRepository.findAll();
  }

  @Cacheable(value = "tenantProfile", key = "#userId")
  public Tenant findByUserId(Long userId) {
    return tenantRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Tenant profile not found"));
  }

  @Transactional
  @CacheEvict(value = "tenantProfile", allEntries = true)
  public Tenant create(TenantRequest request) {
    if (tenantRepository.existsByEmail(request.getEmail())) {
      throw new InvalidInputException("Tenant email already exists");
    }

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new InvalidInputException("User must sign up before linking tenant"));

    Property property = propertyRepository.findById(request.getPropertyId())
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

    Tenant tenant = new Tenant();
    tenant.setName(request.getName());
    tenant.setEmail(request.getEmail());
    tenant.setPhone(request.getPhone());
    tenant.setProperty(property);
    tenant.setUser(user);
    return tenantRepository.save(tenant);
  }
}
