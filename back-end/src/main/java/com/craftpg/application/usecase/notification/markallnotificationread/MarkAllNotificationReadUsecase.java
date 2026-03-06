package com.craftpg.application.usecase.notification.markallnotificationread;

import com.craftpg.domain.model.Notification;
import java.util.List;

public interface MarkAllNotificationReadUsecase {

    List<Notification> execute();
}