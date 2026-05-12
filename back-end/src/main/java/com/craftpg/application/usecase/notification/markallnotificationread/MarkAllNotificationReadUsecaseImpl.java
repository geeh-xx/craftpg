package com.craftpg.application.usecase.notification.markallnotificationread;

import com.craftpg.application.mapper.NotificationMapper;
import com.craftpg.domain.model.notification.Notification;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkAllNotificationReadUsecaseImpl implements MarkAllNotificationReadUsecase {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public List<NotificationResponse> execute() {
        var unreadNotifications = notificationRepository.findAllByUserIdAndReadAtIsNull(currentUserProvider.getCurrentUserId());
        unreadNotifications.forEach(Notification::markRead);
        return notificationRepository.saveAll(unreadNotifications)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }
}