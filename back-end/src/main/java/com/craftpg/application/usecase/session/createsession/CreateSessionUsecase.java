package com.craftpg.application.usecase.session.createsession;

import com.craftpg.domain.model.campaign.CampaignSession;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;

import java.util.UUID;

public interface CreateSessionUsecase {

    CampaignSession execute(final UUID campaignId, final CreateSessionRequest command);
}
