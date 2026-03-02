package com.craftpg.application.usecase.notification.listnotification;

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
class ListNotificationUsecaseImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ListNotificationUsecaseImpl usecase;

    @Test
    void execute_currentUser_returnsNotifications() {
        // Given
        var userId = UUID.randomUUID();
        var notifications = List.of(mock(Notification.class));

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)).thenReturn(notifications);

        // When
        var result = usecase.execute();

        // Then
        assertEquals(1, result.size());
        verify(notificationRepository).findAllByUserIdOrderByCreatedAtDesc(userId);
    }
}
