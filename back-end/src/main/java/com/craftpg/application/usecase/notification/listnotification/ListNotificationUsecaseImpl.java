package com.craftpg.application.usecase.notification.listnotification;

import com.craftpg.application.mapper.NotificationMapper;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListNotificationUsecaseImpl implements ListNotificationUsecase {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> execute() {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(currentUserProvider.getCurrentUserId())
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }
}
