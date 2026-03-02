package com.craftpg.application.usecase.campaign.getcampaign;

import lombok.NonNull;

import com.craftpg.domain.model.Campaign;
import java.util.UUID;

public interface GetCampaignUsecase {

    Campaign execute(@NonNull final UUID campaignId);
}
