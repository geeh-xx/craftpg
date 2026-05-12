package com.craftpg.application.usecase.invite.cancelpendinginvite;

import java.util.UUID;

public interface CancelPendingInviteUsecase {

    void execute(UUID campaignId, UUID inviteId);
}
