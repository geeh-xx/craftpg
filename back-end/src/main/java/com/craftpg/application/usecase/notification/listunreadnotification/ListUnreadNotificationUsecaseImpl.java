package com.craftpg.application.usecase.notification.listunreadnotification;

import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListUnreadNotificationUsecaseImpl implements ListUnreadNotificationUsecase {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional(readOnly = true)
    public List<Notification> execute() {
        return notificationRepository.findAllByUserIdAndReadAtIsNullOrderByCreatedAtDesc(currentUserProvider.getCurrentUserId());
    }
}