package com.craftpg.application.usecase.notification.markallnotificationread;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarkAllNotificationReadUsecaseImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private MarkAllNotificationReadUsecaseImpl usecase;

    @Test
    void execute_unreadNotifications_marksAllAsReadAndSaves() {
        // Given
        var userId = UUID.randomUUID();
        var notificationA = mock(Notification.class);
        var notificationB = mock(Notification.class);
        var unreadNotifications = List.of(notificationA, notificationB);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(notificationRepository.findAllByUserIdAndReadAtIsNull(userId)).thenReturn(unreadNotifications);
        when(notificationRepository.saveAll(unreadNotifications)).thenReturn(unreadNotifications);

        // When
        var result = usecase.execute();

        // Then
        assertEquals(2, result.size());
        verify(notificationA).markRead();
        verify(notificationB).markRead();
        verify(notificationRepository).saveAll(unreadNotifications);
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