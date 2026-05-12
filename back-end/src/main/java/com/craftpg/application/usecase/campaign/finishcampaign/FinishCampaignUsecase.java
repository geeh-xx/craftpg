package com.craftpg.application.usecase.campaign.finishcampaign;

import com.craftpg.application.usecase.OperationResult;
import com.craftpg.infrastructure.web.dto.CampaignResponse;

import java.util.UUID;

public interface FinishCampaignUsecase {

    OperationResult<CampaignResponse> execute(final UUID campaignId);
}
