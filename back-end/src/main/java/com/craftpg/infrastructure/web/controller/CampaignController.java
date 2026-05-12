package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.mapper.CampaignMapper;
import com.craftpg.application.usecase.campaign.CampaignManagementUseCase;
import com.craftpg.application.usecase.campaign.finishcampaign.FinishCampaignUsecase;
import com.craftpg.infrastructure.exception.api.InvalidResultException;
import com.craftpg.infrastructure.factory.UseCaseProvider;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionChecker;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.api.CampaignsApi;
import com.craftpg.infrastructure.web.dto.CampaignPageResponse;
import com.craftpg.infrastructure.web.dto.CampaignPermissionsResponse;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CampaignController implements CampaignsApi {

    private final UseCaseProvider useCaseProvider;
    private final CampaignMapper campaignMapper;
    private final CampaignPermissionChecker campaignPermissionChecker;
    private final CurrentUserProvider currentUserProvider;

    @Override
    public ResponseEntity<CampaignPageResponse> getAllPaginated(final Integer page, final Integer size) {
        var result = useCaseProvider.getUseCase(CampaignManagementUseCase.class).findAll(PageRequest.of(page, size));

        return result.getValue()
                .map(campaignMapper::toPageResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new InvalidResultException(result.getMessage()));
    }

    @Override
    public ResponseEntity<CampaignResponse> create(final CreateCampaignRequest createCampaignRequest) {
        var result = useCaseProvider.getUseCase(CampaignManagementUseCase.class).create(createCampaignRequest);

        return result.getValue()
                .map(response -> ResponseEntity.status(201).body(response))
                .orElseThrow(() -> new InvalidResultException(result.getMessage()));
    }

    @Override
    public ResponseEntity<CampaignResponse> getById(final UUID campaignId) {
        var result = useCaseProvider.getUseCase(CampaignManagementUseCase.class).findById(campaignId);

        return result.getValue()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new InvalidResultException(result.getMessage()));
    }

    @Override
    public ResponseEntity<CampaignResponse> updateById(final UUID campaignId, final UpdateCampaignRequest updateCampaignRequest) {
        updateCampaignRequest.setCampaignId(campaignId);
        var result = useCaseProvider.getUseCase(CampaignManagementUseCase.class).update(updateCampaignRequest);

        return result.getValue()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new InvalidResultException(result.getMessage()));
    }

    @Override
    public ResponseEntity<Void> deleteById(final UUID campaignId) {
        var result = useCaseProvider.getUseCase(CampaignManagementUseCase.class).delete(campaignId);
        if (result.isNotSuccess()) {
            throw new InvalidResultException(result.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CampaignResponse> finishById(final UUID campaignId) {
        var result = useCaseProvider.getUseCase(FinishCampaignUsecase.class).execute(campaignId);
        return result.getValue()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new InvalidResultException(result.getMessage()));
    }

    @Override
    public ResponseEntity<CampaignPermissionsResponse> getPermissionsByCampaignId(final UUID campaignId) {
        final UUID userId = currentUserProvider.getCurrentUserId();
        final var response = new CampaignPermissionsResponse();
        response.setCanManageSessions(campaignPermissionChecker.canEditSession(campaignId, userId));
        response.setCanInvite(campaignPermissionChecker.canInvite(campaignId, userId));
        response.setIsGM(campaignPermissionChecker.canDeleteCampaign(campaignId, userId));
        return ResponseEntity.ok(response);
    }
}
