package com.craftpg.application.usecase.session.listsession;

import com.craftpg.domain.model.campaign.CampaignSession;

import java.util.List;
import java.util.UUID;

public interface ListSessionUsecase {

    List<CampaignSession> execute(final UUID campaignId);
}
