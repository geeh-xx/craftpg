package com.craftpg.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String type;

    @Column(name = "payload_json", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String payloadJson;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static Notification createInviteNotification(
        @NonNull final UUID userId,
        @NonNull final UUID campaignId,
        @NonNull final String email,
        @NonNull final String rolesJson
    ) {
        var notification = new Notification();
        notification.id = UUID.randomUUID();
        notification.userId = userId;
        notification.type = "CAMPAIGN_INVITE";
        notification.payloadJson = "{\"campaignId\":\"" + campaignId + "\",\"email\":\"" + email + "\",\"roles\":" + rolesJson + "}";
        notification.createdAt = LocalDateTime.now();
        return notification;
    }

    public void markRead() {
        this.readAt = LocalDateTime.now();
    }
}
