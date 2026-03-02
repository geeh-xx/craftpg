package com.craftpg.configuration;

import com.craftpg.CraftpgApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(classes = { CraftpgApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("cucumber")
@org.springframework.context.annotation.Import(ContainerTestConfig.SecurityTestConfig.class)
public abstract class ContainerTestConfig {

    @org.springframework.boot.test.context.TestConfiguration
    public static class SecurityTestConfig {
        @org.springframework.context.annotation.Bean
        public org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder() {
            return org.mockito.Mockito.mock(org.springframework.security.oauth2.jwt.JwtDecoder.class);
        }
    }

    @Container
    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("craftpg")
            .withUsername("craftpg")
            .withPassword("craftpg");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void registerDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.liquibase.enabled", () -> "false");
        registry.add("app.invite.base-url", () -> "http://localhost/invites");
        registry.add("app.invite.mail-from", () -> "noreply@craftpg.local");
    }
}
