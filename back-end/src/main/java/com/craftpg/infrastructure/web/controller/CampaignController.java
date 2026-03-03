package com.craftpg.infrastructure.web.controller;

import org.jspecify.annotations.NonNull;

import com.craftpg.application.mapper.CampaignMapper;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionChecker;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.application.usecase.campaign.createcampaign.CreateCampaignUsecase;
import com.craftpg.application.usecase.campaign.deletecampaign.DeleteCampaignUsecase;
import com.craftpg.application.usecase.campaign.finishcampaign.FinishCampaignUsecase;
import com.craftpg.application.usecase.campaign.getcampaign.GetCampaignUsecase;
import com.craftpg.application.usecase.campaign.listcampaign.ListCampaignUsecase;
import com.craftpg.application.usecase.campaign.updatecampaign.UpdateCampaignUsecase;
import com.craftpg.infrastructure.web.api.CampaignsApi;
import com.craftpg.infrastructure.web.dto.CampaignPermissionsResponse;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CampaignController implements CampaignsApi {

    private final ListCampaignUsecase listCampaignUsecase;
    private final CreateCampaignUsecase createCampaignUsecase;
    private final GetCampaignUsecase getCampaignUsecase;
    private final UpdateCampaignUsecase updateCampaignUsecase;
    private final DeleteCampaignUsecase deleteCampaignUsecase;
    private final FinishCampaignUsecase finishCampaignUsecase;
    private final CampaignMapper campaignMapper;
    private final CampaignPermissionChecker campaignPermissionChecker;
    private final CurrentUserProvider currentUserProvider;

    @Override
    public ResponseEntity<List<CampaignResponse>> campaignsGet() {
        return ResponseEntity.ok(listCampaignUsecase.execute().stream().map(campaignMapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<CampaignResponse> campaignsPost(@NonNull final CreateCampaignRequest createCampaignRequest) {
        return ResponseEntity.status(201).body(campaignMapper.toResponse(createCampaignUsecase.execute(createCampaignRequest)));
    }

    @Override
    public ResponseEntity<CampaignResponse> campaignsCampaignIdGet(@NonNull final UUID campaignId) {
        return ResponseEntity.ok(campaignMapper.toResponse(getCampaignUsecase.execute(campaignId)));
    }

    @Override
    public ResponseEntity<CampaignResponse> campaignsCampaignIdPatch(@NonNull final UUID campaignId, @NonNull final UpdateCampaignRequest updateCampaignRequest) {
        return ResponseEntity.ok(campaignMapper.toResponse(updateCampaignUsecase.execute(campaignId, updateCampaignRequest)));
    }

    @Override
    public ResponseEntity<Void> campaignsCampaignIdDelete(@NonNull final UUID campaignId) {
        deleteCampaignUsecase.execute(campaignId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CampaignResponse> campaignsCampaignIdFinishPost(@NonNull final UUID campaignId) {
        return ResponseEntity.ok(campaignMapper.toResponse(finishCampaignUsecase.execute(campaignId)));
    }

    @Override
    public ResponseEntity<CampaignPermissionsResponse> campaignsCampaignIdPermissionsGet(@NonNull final UUID campaignId) {
        final UUID userId = currentUserProvider.getCurrentUserId();
        final var response = new CampaignPermissionsResponse();
        response.setCanManageSessions(campaignPermissionChecker.canEditSession(campaignId, userId));
        response.setCanInvite(campaignPermissionChecker.canInvite(campaignId, userId));
        response.setIsGM(campaignPermissionChecker.canDeleteCampaign(campaignId, userId));
        return ResponseEntity.ok(response);
    }
}
