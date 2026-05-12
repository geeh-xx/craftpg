package com.craftpg.application.usecase.notification.markallnotificationread;

import com.craftpg.infrastructure.web.dto.NotificationResponse;

import java.util.List;

public interface MarkAllNotificationReadUsecase {

    List<NotificationResponse> execute();
}