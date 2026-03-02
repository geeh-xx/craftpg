package com.craftpg.application.usecase.campaign.deletecampaign;

import lombok.NonNull;

import java.util.UUID;

public interface DeleteCampaignUsecase {

    void execute(@NonNull final UUID campaignId);
}
