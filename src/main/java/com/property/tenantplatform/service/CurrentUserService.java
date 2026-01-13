package com.property.tenantplatform.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.property.tenantplatform.exception.ResourceNotFoundException;
import com.property.tenantplatform.model.User;
import com.property.tenantplatform.repository.UserRepository;

@Service
public class CurrentUserService {

  private final UserRepository userRepository;

  public CurrentUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Long getCurrentUserId(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return user.getId();
  }
}
