package com.craftpg.infrastructure.security.currentuser;

import java.net.URI;
import java.util.UUID;

import com.craftpg.infrastructure.web.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class KeycloakUserProfileService {

    private final AuthServiceClient client;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${app.keycloak.admin.realm}")
    private String adminRealm;

    @Value("${app.keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${app.keycloak.admin.username}")
    private String adminUsername;

    @Value("${app.keycloak.admin.password}")
    private String adminPassword;

    public void updateFirstName(final UUID userId, final String firstName) {
        var realm = resolveRealm();
        var adminToken = fetchAdminToken();

        var userRepresentation = client.getUser(realm, userId, "Bearer " + adminToken);

        if (userRepresentation == null) {
            throw new IllegalStateException("failed to load user representation from keycloak");
        }

        userRepresentation.put("firstName", firstName);

        client.updateUser(realm, userId, "Bearer " + adminToken, userRepresentation);
    }

    private String fetchAdminToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", adminClientId);
        form.add("username", adminUsername);
        form.add("password", adminPassword);

        var tokenResponse = client.getAdminToken(adminRealm, form);

        if (tokenResponse == null || tokenResponse.access_token() == null || tokenResponse.access_token().isBlank()) {
            throw new IllegalStateException("failed to obtain keycloak admin token");
        }

        return tokenResponse.access_token();
    }

    private String resolveRealm() {
        var issuer = URI.create(issuerUri);
        var path = issuer.getPath();
        if (path == null) {
            throw new IllegalStateException("invalid keycloak issuer uri");
        }

        var marker = "/realms/";
        var index = path.indexOf(marker);
        if (index < 0 || index + marker.length() >= path.length()) {
            throw new IllegalStateException("invalid keycloak issuer uri");
        }

        return path.substring(index + marker.length()).replace("/", "").trim();
    }
}
