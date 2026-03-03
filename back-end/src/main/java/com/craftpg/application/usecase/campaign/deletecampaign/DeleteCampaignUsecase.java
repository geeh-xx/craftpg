package com.craftpg.application.usecase.campaign.deletecampaign;

import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface DeleteCampaignUsecase {

    void execute(@NonNull final UUID campaignId);
}
