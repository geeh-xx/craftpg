package com.craftpg.application.usecase.invite.createinvite;

import org.jspecify.annotations.NonNull;

import com.craftpg.infrastructure.web.dto.CreateInviteRequest;
import java.util.UUID;

public interface CreateInviteUsecase {

    String execute(@NonNull final UUID campaignId, @NonNull final CreateInviteRequest command);
}
