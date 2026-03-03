package com.craftpg.application.usecase.campaign.getcampaign;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import org.jspecify.annotations.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetCampaignUsecaseImpl implements GetCampaignUsecase {

    private final CampaignRepository campaignRepository;

    @Override
    @Transactional(readOnly = true)
    @RequireCampaignPermission(action = CampaignPermissionAction.VIEW)
    public Campaign execute(@NonNull final UUID campaignId) {
        return campaignRepository.findById(campaignId).orElseThrow(() -> new ApiException("campaign not found"));
    }
}
