package com.property.tenantplatform.dto;

import jakarta.validation.constraints.NotBlank;

public class PropertyRequest {
  @NotBlank
  private String name;

  @NotBlank
  private String address;

  @NotBlank
  private String ownerName;

  public PropertyRequest() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }
}
