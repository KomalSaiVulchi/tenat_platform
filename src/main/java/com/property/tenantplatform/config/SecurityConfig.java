package com.property.tenantplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.property.tenantplatform.security.CustomUserDetailsService;
import com.property.tenantplatform.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomUserDetailsService userDetailsService;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                        CustomUserDetailsService userDetailsService) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
          .requestMatchers(HttpMethod.GET, "/api/properties/**").hasAnyRole("ADMIN", "TENANT")
          .requestMatchers("/api/properties/**").hasRole("ADMIN")

          .requestMatchers(HttpMethod.POST, "/api/tenants/**").hasRole("ADMIN")
          .requestMatchers(HttpMethod.GET, "/api/tenants/me").hasAnyRole("ADMIN", "TENANT")
          .requestMatchers(HttpMethod.GET, "/api/tenants/**").hasRole("ADMIN")

          .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasRole("TENANT")
          .requestMatchers(HttpMethod.GET, "/api/bookings/me").hasRole("TENANT")
          .requestMatchers("/api/bookings/*/decide").hasRole("ADMIN")
          .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasRole("ADMIN")

          .requestMatchers(HttpMethod.POST, "/api/complaints/**").hasRole("TENANT")
          .requestMatchers("/api/complaints/*/status").hasRole("ADMIN")
          .requestMatchers(HttpMethod.GET, "/api/complaints/me").hasRole("TENANT")
          .requestMatchers(HttpMethod.GET, "/api/complaints/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .userDetailsService(userDetailsService);

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
