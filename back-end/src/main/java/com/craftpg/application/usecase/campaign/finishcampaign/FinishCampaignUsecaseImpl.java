package com.craftpg.application.usecase.campaign.finishcampaign;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import lombok.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinishCampaignUsecaseImpl implements FinishCampaignUsecase {

    private final CampaignRepository campaignRepository;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.FINISH)
    public Campaign execute(@NonNull final UUID campaignId) {
        var campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ApiException("campaign not found"));
        campaign.finish();
        return campaignRepository.save(campaign);
    }
}
