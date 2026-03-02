package com.craftpg.application.usecase.campaign.finishcampaign;

import lombok.NonNull;

import com.craftpg.domain.model.Campaign;
import java.util.UUID;

public interface FinishCampaignUsecase {

    Campaign execute(@NonNull final UUID campaignId);
}
