package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.usecase.notification.listnotification.ListNotificationUsecase;
import com.craftpg.application.usecase.notification.listunreadnotification.ListUnreadNotificationUsecase;
import com.craftpg.application.usecase.notification.markallnotificationread.MarkAllNotificationReadUsecase;
import com.craftpg.application.usecase.notification.marknotificationread.MarkNotificationReadUsecase;
import com.craftpg.infrastructure.factory.UseCaseProvider;
import com.craftpg.infrastructure.web.api.NotificationsApi;
import com.craftpg.infrastructure.web.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationsApi {

    private final UseCaseProvider useCaseProvider;

    @Override
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(useCaseProvider.getUseCase(ListNotificationUsecase.class).execute());
    }

    @Override
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        return ResponseEntity.ok(useCaseProvider.getUseCase(ListUnreadNotificationUsecase.class).execute());
    }

    @Override
    public ResponseEntity<List<NotificationResponse>> markAllNotificationsAsRead() {
        return ResponseEntity.ok(useCaseProvider.getUseCase(MarkAllNotificationReadUsecase.class).execute());
    }

    @Override
    public ResponseEntity<NotificationResponse> markNotificationAsReadById(final UUID notificationId) {
        return ResponseEntity.ok(useCaseProvider.getUseCase(MarkNotificationReadUsecase.class).execute(notificationId));
    }
}
