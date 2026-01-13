package com.property.tenantplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TenantPlatformApplication {

  public static void main(String[] args) {
    SpringApplication.run(TenantPlatformApplication.class, args);
  }
}
