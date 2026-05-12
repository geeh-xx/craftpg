package com.craftpg.application.usecase.campaign;

import com.craftpg.application.mapper.CampaignMapper;
import com.craftpg.application.usecase.OperationResult;
import com.craftpg.domain.model.campaign.Campaign;
import com.craftpg.domain.model.campaign.CampaignID;
import com.craftpg.domain.model.campaign.CampaignRole;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionChecker;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import com.craftpg.shared.constants.CampaignRoleType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignManagementUseCaseImpl implements CampaignManagementUseCase {

    private final CampaignRepository campaignRepository;
    private final CampaignRoleRepository campaignRoleRepository;
    private final CurrentUserProvider currentUserProvider;
    private final CampaignPermissionChecker campaignPermissionChecker;
    private final CampaignMapper campaignMapper;

    @Override
    @Transactional
    public OperationResult<CampaignResponse> create(@NonNull final CreateCampaignRequest request) {
        var userId = currentUserProvider.getCurrentUserId();
        var campaign = campaignRepository.save(Campaign.create(campaignMapper.toCreateInput(request, userId)));
        campaignRoleRepository.save(CampaignRole.create(campaign.getId().getValue(), userId, CampaignRoleType.DM));
        return OperationResult.ok(campaignMapper.toResponse(campaign));
    }

    @Override
    @Transactional
    public OperationResult<CampaignResponse> update(@NonNull final UpdateCampaignRequest request) {
        var campaignId = request.getCampaignId();
        var userId = currentUserProvider.getCurrentUserId();
        if (!campaignPermissionChecker.canEditCampaign(campaignId, userId)) {
            return OperationResult.failure("forbidden");
        }

        var campaignOptional = campaignRepository.findById(CampaignID.of(campaignId));
        if (campaignOptional.isEmpty()) {
            return OperationResult.failure("campaign not found");
        }

        var campaign = campaignOptional.get();
        campaign.update(
                request.getTitle(),
                request.getDescription(),
                request.getFrequency(),
                request.getStatus(),
                request.getProgressPercent()
        );

        var updatedCampaign = campaignRepository.save(campaign);
        return OperationResult.ok(campaignMapper.toResponse(updatedCampaign));
    }

    @Override
    @Transactional(readOnly = true)
    @RequireCampaignPermission(action = CampaignPermissionAction.VIEW)
    public OperationResult<CampaignResponse> findById(@NonNull final UUID uuid) {
        var campaignOptional = campaignRepository.findById(CampaignID.of(uuid));
        if (campaignOptional.isEmpty()) {
            return OperationResult.failure("campaign not found");
        }
        return OperationResult.ok(campaignMapper.toResponse(campaignOptional.get()));
    }

    @Override
    @Transactional(readOnly = true)
    public OperationResult<Page<CampaignResponse>> findAll(@NonNull final Pageable pageable) {

        var userId = currentUserProvider.getCurrentUserId();
        var campaigns = campaignRepository.findPageByUserId(userId, pageable)
                .map(campaignMapper::toResponse);

        return OperationResult.ok(campaigns);
    }

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.DELETE)
    public OperationResult<Void> delete(@NonNull final UUID uuid) {
        if (!campaignRepository.existsById(CampaignID.of(uuid))) {
            return OperationResult.failure("campaign not found");
        }
        campaignRepository.deleteById(CampaignID.of(uuid));
        return OperationResult.ok();
    }
}
