package com.craftpg.configuration;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class BeanTestConfig {


    @Bean
    SecurityFilterChain testChain(HttpSecurity http) {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return token -> Jwt
            .withTokenValue(token)
            .header("alg", "none")
            .subject(UUID.fromString("00000000-0000-0000-0000-000000000001").toString())
            .claim("email", "cucumber@craftpg.test")
            .claim("preferred_username", "cucumber-user")
            .claims(claims -> claims.put("scope", "openid"))
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();
    }
}
