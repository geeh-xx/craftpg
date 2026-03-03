package com.craftpg.application.usecase.invite.getinvitebytoken;

import org.jspecify.annotations.NonNull;

public interface GetInviteByTokenUsecase {

    InvitePreviewData execute(@NonNull final String token);
}
