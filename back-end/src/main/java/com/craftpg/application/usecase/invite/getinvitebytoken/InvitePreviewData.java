package com.craftpg.application.usecase.invite.getinvitebytoken;

import com.craftpg.domain.model.campaign.CampaignInvite;

public record InvitePreviewData(CampaignInvite invite, String campaignTitle, String dmName
) {
}
