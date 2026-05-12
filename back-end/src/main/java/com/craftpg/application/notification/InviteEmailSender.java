package com.craftpg.application.notification;

import lombok.NonNull;

public interface InviteEmailSender {

    void sendInviteEmail(@NonNull final String email, @NonNull final String inviteToken);
}
