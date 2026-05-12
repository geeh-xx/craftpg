package com.craftpg.application.usecase.notification.marknotificationread;

import com.craftpg.application.mapper.NotificationMapper;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.NotificationResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarkNotificationReadUsecaseImpl implements MarkNotificationReadUsecase {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public NotificationResponse execute(@NonNull final UUID notificationId) {
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException("notification not found"));

        if (!notification.getUserId().equals(currentUserProvider.getCurrentUserId())) {
            throw new ApiException("forbidden");
        }

        notification.markRead();
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }
}
