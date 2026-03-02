package com.craftpg.application.usecase.invite.getinvitebytoken;

import lombok.NonNull;

public interface GetInviteByTokenUsecase {

    InvitePreviewData execute(@NonNull final String token);
}
