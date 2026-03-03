package com.craftpg.application.mapper;

import org.jspecify.annotations.NonNull;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper {

    public CampaignResponse toResponse(@NonNull final Campaign campaign) {
        return new CampaignResponse(
            campaign.getId(),
            campaign.getTitle(),
            campaign.getSystem(),
            campaign.getFrequency(),
            campaign.getStatus(),
            campaign.getProgressPercent()
        ).description(campaign.getDescription());
    }
}
