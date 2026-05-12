package com.craftpg.application.usecase.notification.marknotificationread;

import com.craftpg.infrastructure.web.dto.NotificationResponse;

import java.util.UUID;

public interface MarkNotificationReadUsecase {

    NotificationResponse execute(final UUID notificationId);
}
