package com.craftpg.application.usecase.invite.getinvitebytoken;

import com.craftpg.domain.model.CampaignInvite;
import org.jspecify.annotations.NonNull;

public record InvitePreviewData(
    @NonNull CampaignInvite invite,
    @NonNull String campaignTitle,
    @NonNull String dmName
) {
}
