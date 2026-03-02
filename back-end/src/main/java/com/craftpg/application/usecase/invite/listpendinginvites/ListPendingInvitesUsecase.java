package com.craftpg.application.usecase.invite.listpendinginvites;

import com.craftpg.domain.model.CampaignInvite;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;

public interface ListPendingInvitesUsecase {

    List<CampaignInvite> execute(@NonNull UUID campaignId);
}
