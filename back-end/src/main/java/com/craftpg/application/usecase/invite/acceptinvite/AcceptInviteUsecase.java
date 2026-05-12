package com.craftpg.application.usecase.invite.acceptinvite;

import java.util.UUID;

public interface AcceptInviteUsecase {

    UUID execute(final String token, final UUID characterBaseId);
}
