package com.craftpg.application.usecase.notification.markallnotificationread;

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
class MarkAllNotificationReadUsecaseImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private MarkAllNotificationReadUsecaseImpl usecase;

    @Test
    void execute_unreadNotifications_marksAllAsReadAndSaves() {
        // Given
        var userId = UUID.randomUUID();
        var notificationA = mock(Notification.class);
        var notificationB = mock(Notification.class);
        var unreadNotifications = List.of(notificationA, notificationB);
        var responseA = mock(NotificationResponse.class);
        var responseB = mock(NotificationResponse.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(notificationRepository.findAllByUserIdAndReadAtIsNull(userId)).thenReturn(unreadNotifications);
        when(notificationRepository.saveAll(unreadNotifications)).thenReturn(unreadNotifications);
        when(notificationMapper.toResponse(notificationA)).thenReturn(responseA);
        when(notificationMapper.toResponse(notificationB)).thenReturn(responseB);

        // When
        var result = usecase.execute();

        // Then
        assertEquals(2, result.size());
        verify(notificationA).markRead();
        verify(notificationB).markRead();
        verify(notificationRepository).saveAll(unreadNotifications);
        verify(notificationMapper).toResponse(notificationA);
        verify(notificationMapper).toResponse(notificationB);
    }

    @Test
    void execute_noUnreadNotifications_returnsEmptyList() {
        // Given
        var userId = UUID.randomUUID();
        var emptyList = List.<Notification>of();

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(notificationRepository.findAllByUserIdAndReadAtIsNull(userId)).thenReturn(emptyList);
        when(notificationRepository.saveAll(emptyList)).thenReturn(emptyList);

        // When
        var result = usecase.execute();

        // Then
        assertEquals(0, result.size());
        verify(notificationRepository).saveAll(emptyList);
    }
}