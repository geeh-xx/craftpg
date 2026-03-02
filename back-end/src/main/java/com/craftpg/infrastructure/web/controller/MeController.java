package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.mapper.MeMapper;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.security.currentuser.AppUserSyncService;
import com.craftpg.infrastructure.web.api.UsersApi;
import com.craftpg.infrastructure.web.dto.MeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController implements UsersApi {

    private final AppUserSyncService appUserSyncService;
    private final MeMapper meMapper;

    @Override
    public ResponseEntity<MeResponse> meGet() {
        var user = appUserSyncService
            .syncFromSecurityContext()
            .orElseThrow(() -> new ApiException("unauthorized"));

        return ResponseEntity.ok(meMapper.toResponse(user));
    }
}
