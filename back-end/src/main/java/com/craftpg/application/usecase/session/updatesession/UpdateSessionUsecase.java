package com.craftpg.application.usecase.session.updatesession;

import com.craftpg.domain.model.campaign.CampaignSession;
import com.craftpg.infrastructure.web.dto.UpdateSessionRequest;

import java.util.UUID;

public interface UpdateSessionUsecase {

    CampaignSession execute(final UUID campaignId, final UUID sessionId, final UpdateSessionRequest command);
}
