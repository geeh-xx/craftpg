package com.craftpg.application.usecase.notification.marknotificationread;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarkNotificationReadUsecaseImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private MarkNotificationReadUsecaseImpl usecase;

    @Test
    void execute_ownedNotification_marksAsReadAndSaves() {
        // Given
        var notificationId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var notification = mock(Notification.class);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notification.getUserId()).thenReturn(userId);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(notificationRepository.save(notification)).thenReturn(notification);

        // When
        var result = usecase.execute(notificationId);

        // Then
        assertSame(notification, result);
        verify(notification).markRead();
        verify(notificationRepository).save(notification);
    }

    @Test
    void execute_notificationNotFound_throwsApiException() {
        // Given
        var notificationId = UUID.randomUUID();
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(notificationId));

        // Then
        assertEquals("notification not found", exception.getMessage());
    }

    @Test
    void execute_foreignNotification_throwsApiException() {
        // Given
        var notificationId = UUID.randomUUID();
        var notification = mock(Notification.class);
        var ownerId = UUID.randomUUID();
        var currentUserId = UUID.randomUUID();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notification.getUserId()).thenReturn(ownerId);
        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(notificationId));

        // Then
        assertEquals("forbidden", exception.getMessage());
    }
}
