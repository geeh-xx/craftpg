package com.craftpg.application.usecase.notification.marknotificationread;

import com.craftpg.domain.model.Notification;
import lombok.NonNull;
import java.util.UUID;

public interface MarkNotificationReadUsecase {

    Notification execute(@NonNull final UUID notificationId);
}
