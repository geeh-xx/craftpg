package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.mapper.MeMapper;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.security.currentuser.AppUserSyncService;
import com.craftpg.infrastructure.security.currentuser.KeycloakUserProfileService;
import com.craftpg.infrastructure.web.api.UsersApi;
import com.craftpg.infrastructure.web.dto.MeResponse;
import com.craftpg.infrastructure.web.dto.UpdateMeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController implements UsersApi {

    private final AppUserSyncService appUserSyncService;
    private final AppUserRepository appUserRepository;
    private final KeycloakUserProfileService keycloakUserProfileService;
    private final MeMapper meMapper;

    @Override
    public ResponseEntity<MeResponse> meGet() {
        var user = appUserSyncService
            .syncFromSecurityContext()
            .orElseThrow(() -> new ApiException("unauthorized"));

        return ResponseEntity.ok(meMapper.toResponse(user));
    }

    @Override
    public ResponseEntity<MeResponse> mePut(final UpdateMeRequest updateMeRequest) {
        var user = appUserSyncService
            .syncFromSecurityContext()
            .orElseThrow(() -> new ApiException("unauthorized"));

        var displayName = updateMeRequest.getDisplayName() == null
            ? ""
            : updateMeRequest.getDisplayName().trim();

        if (displayName.isBlank()) {
            throw new ApiException("displayName must not be blank");
        }

        keycloakUserProfileService.updateFirstName(user.getId(), displayName);
        user.updateDisplayName(displayName);
        appUserRepository.save(user);

        return ResponseEntity.ok(meMapper.toResponse(user));
    }
}
