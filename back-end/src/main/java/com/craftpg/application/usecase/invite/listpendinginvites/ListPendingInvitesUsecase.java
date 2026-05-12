package com.craftpg.application.usecase.invite.listpendinginvites;

import com.craftpg.domain.model.campaign.CampaignInvite;

import java.util.List;
import java.util.UUID;

public interface ListPendingInvitesUsecase {

    List<CampaignInvite> execute(UUID campaignId);
}
