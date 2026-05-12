package com.craftpg.application.usecase.campaign.finishcampaign;

import com.craftpg.application.mapper.CampaignMapper;
import com.craftpg.application.usecase.OperationResult;
import com.craftpg.domain.model.campaign.CampaignID;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinishCampaignUsecaseImpl implements FinishCampaignUsecase {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.FINISH)
    public OperationResult<CampaignResponse> execute(@NonNull final UUID campaignId) {

        return campaignRepository.findById(CampaignID.of(campaignId))
                .map(campaign -> {
                    campaign.finish();

                    var savedCampaign = campaignRepository.save(campaign);
                    var response = campaignMapper.toResponse(savedCampaign);

                    return OperationResult.ok(response);
                })
                .orElseGet(() -> OperationResult.error("campaign not found"));
    }
}
