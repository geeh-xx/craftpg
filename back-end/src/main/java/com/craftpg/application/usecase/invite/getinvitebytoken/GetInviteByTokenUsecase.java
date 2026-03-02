package com.craftpg.application.usecase.invite.getinvitebytoken;

import lombok.NonNull;

import com.craftpg.domain.model.CampaignInvite;

public interface GetInviteByTokenUsecase {

    CampaignInvite execute(@NonNull final String token);
}
