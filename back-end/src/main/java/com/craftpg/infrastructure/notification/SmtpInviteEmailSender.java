package com.craftpg.infrastructure.notification;

import com.craftpg.application.notification.InviteEmailSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmtpInviteEmailSender implements InviteEmailSender {

    private final JavaMailSender javaMailSender;

    @Value("${app.invite.base-url}")
    private String inviteBaseUrl;

    @Value("${app.invite.mail-from}")
    private String mailFrom;

    @Override
    public void sendInviteEmail(@NonNull final String email, @NonNull final String inviteToken) {
        try {
            var message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(email);
            message.setSubject("CraftPG campaign invite");
            message.setText("You were invited to a campaign. Accept it here: " + inviteBaseUrl + "/" + inviteToken);
            javaMailSender.send(message);
        } catch (Exception ex) {
            log.warn("failed to send invite email to {}", email, ex);
        }
    }
}
