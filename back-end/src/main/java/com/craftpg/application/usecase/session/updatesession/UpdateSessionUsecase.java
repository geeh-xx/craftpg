package com.craftpg.application.usecase.session.updatesession;

import com.craftpg.domain.model.CampaignSession;
import com.craftpg.infrastructure.web.dto.UpdateSessionRequest;
import lombok.NonNull;
import java.util.UUID;

public interface UpdateSessionUsecase {

    CampaignSession execute(@NonNull final UUID campaignId, @NonNull final UUID sessionId, @NonNull final UpdateSessionRequest command);
}
