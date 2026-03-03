package com.craftpg.infrastructure.security.currentuser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.infrastructure.web.client.AuthServiceClient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class KeycloakUserProfileServiceTest {

    @Mock
    private AuthServiceClient authServiceClient;

    private KeycloakUserProfileService service;

    @BeforeEach
    void setUp() {
        service = new KeycloakUserProfileService(authServiceClient);
        ReflectionTestUtils.setField(service, "issuerUri", "http://localhost:8081/realms/craftpg");
        ReflectionTestUtils.setField(service, "adminRealm", "master");
        ReflectionTestUtils.setField(service, "adminClientId", "admin-cli");
        ReflectionTestUtils.setField(service, "adminUsername", "admin");
        ReflectionTestUtils.setField(service, "adminPassword", "admin");
    }

    @Test
    void updateFirstName_validPayload_updatesRepresentation() {
        var userId = UUID.randomUUID();
        var representation = new HashMap<String, Object>();
        representation.put("email", "user@craftpg.test");

        when(authServiceClient.getAdminToken(anyString(), any()))
            .thenReturn(new AuthServiceClient.KeycloakTokenResponse("token-value"));
        when(authServiceClient.getUser(anyString(), any(UUID.class), anyString()))
            .thenReturn(representation);

        service.updateFirstName(userId, "Updated Name");

        assertEquals("Updated Name", representation.get("firstName"));
        verify(authServiceClient).updateUser("craftpg", userId, "Bearer token-value", representation);
    }

    @Test
    void updateFirstName_missingAccessToken_throws() {
        when(authServiceClient.getAdminToken(anyString(), any()))
            .thenReturn(new AuthServiceClient.KeycloakTokenResponse(null));

        assertThrows(IllegalStateException.class, () -> service.updateFirstName(UUID.randomUUID(), "Name"));
    }

    @Test
    void updateFirstName_missingUserRepresentation_throws() {
        when(authServiceClient.getAdminToken(anyString(), any()))
            .thenReturn(new AuthServiceClient.KeycloakTokenResponse("token-value"));
        when(authServiceClient.getUser(anyString(), any(UUID.class), anyString()))
            .thenReturn(null);

        assertThrows(IllegalStateException.class, () -> service.updateFirstName(UUID.randomUUID(), "Name"));
    }

    @Test
    void updateFirstName_invalidIssuerRealm_throws() {
        ReflectionTestUtils.setField(service, "issuerUri", "http://localhost:8081");

        assertThrows(IllegalStateException.class, () -> service.updateFirstName(UUID.randomUUID(), "Name"));
    }
}
