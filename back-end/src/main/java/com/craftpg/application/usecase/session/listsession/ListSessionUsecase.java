package com.craftpg.application.usecase.session.listsession;

import com.craftpg.domain.model.CampaignSession;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.UUID;

public interface ListSessionUsecase {

    List<CampaignSession> execute(@NonNull final UUID campaignId);
}
