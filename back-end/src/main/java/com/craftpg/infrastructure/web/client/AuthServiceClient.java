package com.craftpg.infrastructure.web.client;

import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE)
public interface AuthServiceClient {

    @PostExchange(value = "/realms/{realm}/protocol/openid-connect/token", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KeycloakTokenResponse getAdminToken(
        @PathVariable String realm,
        @RequestBody MultiValueMap<String, String> form
    );

    @GetExchange("/admin/realms/{realm}/users/{userId}")
    Map<String, Object> getUser(
        @PathVariable String realm,
        @PathVariable UUID userId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );

    @PutExchange(value = "/admin/realms/{realm}/users/{userId}", contentType = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(
        @PathVariable String realm,
        @PathVariable UUID userId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestBody Map<String, Object> userRepresentation
    );

    record KeycloakTokenResponse(String access_token) {
    }
}
