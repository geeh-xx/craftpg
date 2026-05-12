package com.craftpg.application.usecase.notification.listnotification;

import com.craftpg.infrastructure.web.dto.NotificationResponse;

import java.util.List;

public interface ListNotificationUsecase {

    List<NotificationResponse> execute();
}
