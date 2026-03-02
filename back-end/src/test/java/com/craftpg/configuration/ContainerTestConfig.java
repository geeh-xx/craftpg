package com.craftpg.configuration;

import com.craftpg.CraftpgApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(classes = {CraftpgApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("cucumber")
public abstract class ContainerTestConfig {

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
        registry.add("spring.liquibase.enabled", () -> "true");
        registry.add("spring.liquibase.change-log", () -> "classpath:db/changelogs/db.changelog-master.sql");
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> "http://cucumber-test-issuer");
        registry.add("app.invite.base-url", () -> "http://localhost/invites");
        registry.add("app.invite.mail-from", () -> "noreply@craftpg.local");
    }
}
