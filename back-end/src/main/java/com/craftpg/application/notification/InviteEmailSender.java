package com.craftpg.application.notification;

import org.jspecify.annotations.NonNull;

public interface InviteEmailSender {

    void sendInviteEmail(@NonNull final String email, @NonNull final String inviteToken);
}
