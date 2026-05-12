package com.craftpg.application.usecase.notification.listnotification;

import com.craftpg.application.mapper.NotificationMapper;
import com.craftpg.domain.model.notification.Notification;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.NotificationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListNotificationUsecaseImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private ListNotificationUsecaseImpl usecase;

    @Test
    void execute_currentUser_returnsNotifications() {
        // Given
        var userId = UUID.randomUUID();
        var notification = mock(Notification.class);
        var notifications = List.of(notification);
        var response = mock(NotificationResponse.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)).thenReturn(notifications);
        when(notificationMapper.toResponse(notification)).thenReturn(response);

        // When
        var result = usecase.execute();

        // Then
        assertEquals(1, result.size());
        verify(notificationRepository).findAllByUserIdOrderByCreatedAtDesc(userId);
        verify(notificationMapper).toResponse(notification);
    }
}
