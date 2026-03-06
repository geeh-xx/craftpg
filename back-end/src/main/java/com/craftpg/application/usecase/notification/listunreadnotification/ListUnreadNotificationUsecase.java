package com.craftpg.application.usecase.notification.listunreadnotification;

import com.craftpg.domain.model.Notification;
import java.util.List;

public interface ListUnreadNotificationUsecase {

    List<Notification> execute();
}