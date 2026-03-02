package com.craftpg.application.usecase.invite.getinvitebytoken;

import com.craftpg.domain.model.CampaignInvite;
import lombok.NonNull;

public record InvitePreviewData(
    @NonNull CampaignInvite invite,
    @NonNull String campaignTitle,
    @NonNull String dmName
) {
}
