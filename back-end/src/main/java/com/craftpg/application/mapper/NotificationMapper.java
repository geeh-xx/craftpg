package com.craftpg.application.mapper;

import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.web.dto.NotificationResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    @NonNull
    public NotificationResponse toResponse(@NonNull final Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getUserId(),
            notification.getType(),
            notification.getPayloadJson(),
            notification.getCreatedAt(),
            notification.getReadAt() != null
        ).readAt(notification.getReadAt());
    }
}
