package com.property.tenantplatform.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.property.tenantplatform.dto.AuthResponse;
import com.property.tenantplatform.dto.LoginRequest;
import com.property.tenantplatform.dto.SignupRequest;
import com.property.tenantplatform.exception.InvalidInputException;
import com.property.tenantplatform.model.Role;
import com.property.tenantplatform.model.User;
import com.property.tenantplatform.repository.UserRepository;
import com.property.tenantplatform.security.JwtTokenProvider;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
  }

  @Transactional
  public AuthResponse signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new InvalidInputException("Email already registered");
    }

    Role role = request.getRole();
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(role);

    userRepository.save(user);

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    String token = tokenProvider.generateToken(authentication);
    return new AuthResponse(token, role);
  }

  public AuthResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    String token = tokenProvider.generateToken(authentication);
    Role role = userRepository.findByEmail(request.getEmail())
        .map(User::getRole)
        .orElse(Role.TENANT);
    return new AuthResponse(token, role);
  }
}
