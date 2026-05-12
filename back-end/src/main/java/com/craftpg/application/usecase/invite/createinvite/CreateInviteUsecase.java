package com.craftpg.application.usecase.invite.createinvite;

import com.craftpg.infrastructure.web.dto.CreateInviteRequest;

import java.util.UUID;

public interface CreateInviteUsecase {

    String execute(final UUID campaignId, final CreateInviteRequest command);
}
