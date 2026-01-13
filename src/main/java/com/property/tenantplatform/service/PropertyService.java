package com.property.tenantplatform.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.property.tenantplatform.dto.PropertyRequest;
import com.property.tenantplatform.exception.ResourceNotFoundException;
import com.property.tenantplatform.model.Property;
import com.property.tenantplatform.repository.PropertyRepository;

@Service
public class PropertyService {

  private final PropertyRepository propertyRepository;

  public PropertyService(PropertyRepository propertyRepository) {
    this.propertyRepository = propertyRepository;
  }

  @Cacheable(value = "properties")
  public List<Property> findAll() {
    return propertyRepository.findAll();
  }

  @Transactional
  @CacheEvict(value = "properties", allEntries = true)
  public Property create(PropertyRequest request) {
    Property property = new Property();
    property.setName(request.getName());
    property.setAddress(request.getAddress());
    property.setOwnerName(request.getOwnerName());
    return propertyRepository.save(property);
  }

  @Transactional
  @CacheEvict(value = "properties", allEntries = true)
  public Property update(Long id, PropertyRequest request) {
    Property property = propertyRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    property.setName(request.getName());
    property.setAddress(request.getAddress());
    property.setOwnerName(request.getOwnerName());
    return propertyRepository.save(property);
  }

  @Transactional
  @CacheEvict(value = "properties", allEntries = true)
  public void delete(Long id) {
    if (!propertyRepository.existsById(id)) {
      throw new ResourceNotFoundException("Property not found");
    }
    propertyRepository.deleteById(id);
  }

  public Property getById(Long id) {
    return propertyRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
  }
}
