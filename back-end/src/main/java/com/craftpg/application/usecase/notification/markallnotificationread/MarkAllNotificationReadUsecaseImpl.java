package com.craftpg.application.usecase.notification.markallnotificationread;

import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarkAllNotificationReadUsecaseImpl implements MarkAllNotificationReadUsecase {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public List<Notification> execute() {
        var unreadNotifications = notificationRepository.findAllByUserIdAndReadAtIsNull(currentUserProvider.getCurrentUserId());
        unreadNotifications.forEach(Notification::markRead);
        return notificationRepository.saveAll(unreadNotifications);
    }
}