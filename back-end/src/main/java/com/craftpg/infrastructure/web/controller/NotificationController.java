package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.mapper.NotificationMapper;
import com.craftpg.application.usecase.notification.listnotification.ListNotificationUsecase;
import com.craftpg.application.usecase.notification.marknotificationread.MarkNotificationReadUsecase;
import com.craftpg.infrastructure.web.api.NotificationsApi;
import com.craftpg.infrastructure.web.dto.NotificationResponse;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationsApi {

    private final ListNotificationUsecase listNotificationUsecase;
    private final MarkNotificationReadUsecase markNotificationReadUsecase;
    private final NotificationMapper notificationMapper;

    @Override
    public ResponseEntity<List<NotificationResponse>> notificationsGet() {
        return ResponseEntity.ok(listNotificationUsecase.execute().stream().map(notificationMapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<NotificationResponse> notificationsNotificationIdReadPost(@NonNull final UUID notificationId) {
        return ResponseEntity.ok(notificationMapper.toResponse(markNotificationReadUsecase.execute(notificationId)));
    }
}
