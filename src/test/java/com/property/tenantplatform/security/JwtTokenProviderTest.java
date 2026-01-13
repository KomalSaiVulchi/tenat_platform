package com.property.tenantplatform.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

class JwtTokenProviderTest {

  @Test
  void shouldGenerateAndParseToken() {
    JwtTokenProvider provider = new JwtTokenProvider();
    byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
    String secret = Encoders.BASE64.encode(keyBytes);
    ReflectionTestUtils.setField(provider, "jwtSecret", secret);
    ReflectionTestUtils.setField(provider, "jwtExpirationMs", 3600000L);

    UserDetails user = User.withUsername("test@example.com").password("pass").roles("TENANT").build();
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        user, null, user.getAuthorities());

    String token = provider.generateToken(authentication);
    String subject = provider.getUsernameFromJWT(token);

    assertThat(subject).isEqualTo("test@example.com");
    assertThat(provider.validateToken(token)).isTrue();
  }
}
