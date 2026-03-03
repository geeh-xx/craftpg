package com.craftpg.application.usecase.notification.marknotificationread;

import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import org.jspecify.annotations.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarkNotificationReadUsecaseImpl implements MarkNotificationReadUsecase {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public Notification execute(@NonNull final UUID notificationId) {
        var notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ApiException("notification not found"));

        if (!notification.getUserId().equals(currentUserProvider.getCurrentUserId())) {
            throw new ApiException("forbidden");
        }

        notification.markRead();
        return notificationRepository.save(notification);
    }
}
