package com.craftpg.application.usecase.invite.getinvitebytoken;

public interface GetInviteByTokenUsecase {

    InvitePreviewData execute(final String token);
}
