package com.craftpg.application.usecase.notification.listunreadnotification;

import com.craftpg.infrastructure.web.dto.NotificationResponse;

import java.util.List;

public interface ListUnreadNotificationUsecase {

    List<NotificationResponse> execute();
}