package com.craftpg.application.usecase.notification.listnotification;

import com.craftpg.domain.model.Notification;
import java.util.List;

public interface ListNotificationUsecase {

    List<Notification> execute();
}
