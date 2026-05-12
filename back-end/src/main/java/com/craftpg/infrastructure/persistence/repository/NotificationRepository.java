package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Notification> findAllByUserIdAndReadAtIsNullOrderByCreatedAtDesc(UUID userId);

    List<Notification> findAllByUserIdAndReadAtIsNull(UUID userId);
}
