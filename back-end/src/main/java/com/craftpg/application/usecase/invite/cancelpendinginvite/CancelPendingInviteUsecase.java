package com.craftpg.application.usecase.invite.cancelpendinginvite;

import java.util.UUID;
import lombok.NonNull;

public interface CancelPendingInviteUsecase {

    void execute(@NonNull UUID campaignId, @NonNull UUID inviteId);
}
