package com.craftpg.application.usecase.campaign.updatecampaign;

import org.jspecify.annotations.NonNull;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import java.util.UUID;

public interface UpdateCampaignUsecase {

    Campaign execute(@NonNull final UUID campaignId, @NonNull final UpdateCampaignRequest command);
}
