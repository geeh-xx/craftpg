package com.craftpg.application.usecase.campaign.updatecampaign;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import org.jspecify.annotations.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCampaignUsecaseImpl implements UpdateCampaignUsecase {

    private final CampaignRepository campaignRepository;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.EDIT)
    public Campaign execute(@NonNull final UUID campaignId, @NonNull final UpdateCampaignRequest command) {
        var campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ApiException("campaign not found"));
        campaign.update(command.getTitle(), command.getDescription(), command.getFrequency(), command.getStatus(), command.getProgressPercent());
        return campaignRepository.save(campaign);
    }
}
