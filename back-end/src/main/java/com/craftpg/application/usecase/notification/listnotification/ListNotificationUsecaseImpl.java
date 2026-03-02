package com.craftpg.application.usecase.notification.listnotification;

import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListNotificationUsecaseImpl implements ListNotificationUsecase {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional(readOnly = true)
    public List<Notification> execute() {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(currentUserProvider.getCurrentUserId());
    }
}
