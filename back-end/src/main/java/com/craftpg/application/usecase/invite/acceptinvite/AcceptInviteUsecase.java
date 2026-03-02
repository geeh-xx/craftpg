package com.craftpg.application.usecase.invite.acceptinvite;

import lombok.NonNull;

import java.util.UUID;

public interface AcceptInviteUsecase {

    UUID execute(@NonNull final String token, @NonNull final UUID characterBaseId);
}
