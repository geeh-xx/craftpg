package com.craftpg.application.usecase.session.createsession;

import com.craftpg.domain.model.CampaignSession;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public interface CreateSessionUsecase {

    CampaignSession execute(@NonNull final UUID campaignId, @NonNull final CreateSessionRequest command);
}
