package com.craftpg.application.usecase.campaign.createcampaign;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import org.jspecify.annotations.NonNull;

public interface CreateCampaignUsecase {

    Campaign execute(@NonNull final CreateCampaignRequest command);
}
