package com.craftpg.infrastructure.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.application.mapper.MeMapper;
import com.craftpg.domain.model.AppUser;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.security.currentuser.AppUserSyncService;
import com.craftpg.infrastructure.security.currentuser.KeycloakUserProfileService;
import com.craftpg.infrastructure.web.dto.MeResponse;
import com.craftpg.infrastructure.web.dto.UpdateMeRequest;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeControllerTest {

    @Mock
    private AppUserSyncService appUserSyncService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private KeycloakUserProfileService keycloakUserProfileService;

    @Mock
    private MeMapper meMapper;

    @InjectMocks
    private MeController meController;

    @Test
    void mePut_validDisplayName_updatesLocalAndKeycloak() {
        var userId = UUID.randomUUID();
        var user = AppUser.create(userId, "user@craftpg.test", "Old Name");
        var request = new UpdateMeRequest();
        request.setDisplayName("  New Display Name  ");

        var expectedResponse = new MeResponse(userId, "user@craftpg.test", "New Display Name");

        when(appUserSyncService.syncFromSecurityContext()).thenReturn(Optional.of(user));
        when(meMapper.toResponse(user)).thenReturn(expectedResponse);

        var response = meController.mePut(request);

        assertSame(expectedResponse, response.getBody());
        assertEquals("New Display Name", user.getDisplayName());
        verify(keycloakUserProfileService).updateFirstName(userId, "New Display Name");
        verify(appUserRepository).save(user);
    }

    @Test
    void mePut_blankDisplayName_throwsApiException() {
        var user = AppUser.create(UUID.randomUUID(), "user@craftpg.test", "Old Name");
        var request = new UpdateMeRequest();
        request.setDisplayName("   ");

        when(appUserSyncService.syncFromSecurityContext()).thenReturn(Optional.of(user));

        assertThrows(ApiException.class, () -> meController.mePut(request));

        verify(keycloakUserProfileService, never()).updateFirstName(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
        verify(appUserRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void mePut_unauthorized_throwsApiException() {
        var request = new UpdateMeRequest();
        request.setDisplayName("Any Name");

        when(appUserSyncService.syncFromSecurityContext()).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> meController.mePut(request));

        verify(keycloakUserProfileService, never()).updateFirstName(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
        verify(appUserRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
