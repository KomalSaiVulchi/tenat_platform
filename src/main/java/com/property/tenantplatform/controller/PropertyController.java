package com.property.tenantplatform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.tenantplatform.dto.PropertyRequest;
import com.property.tenantplatform.model.Property;
import com.property.tenantplatform.service.PropertyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

  private final PropertyService propertyService;

  public PropertyController(PropertyService propertyService) {
    this.propertyService = propertyService;
  }

  @GetMapping
  public ResponseEntity<List<Property>> list() {
    return ResponseEntity.ok(propertyService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Property> get(@PathVariable Long id) {
    return ResponseEntity.ok(propertyService.getById(id));
  }

  @PostMapping
  public ResponseEntity<Property> create(@Valid @RequestBody PropertyRequest request) {
    return ResponseEntity.ok(propertyService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Property> update(@PathVariable Long id,
                                         @Valid @RequestBody PropertyRequest request) {
    return ResponseEntity.ok(propertyService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    propertyService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
