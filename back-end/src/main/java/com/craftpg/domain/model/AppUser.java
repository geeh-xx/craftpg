package com.craftpg.domain.model;

import org.jspecify.annotations.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppUser {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static AppUser create(@NonNull final UUID id, @NonNull final String email, @NonNull final String displayName) {
        var now = LocalDateTime.now();
        var user = new AppUser();
        user.id = id;
        user.email = email;
        user.displayName = displayName;
        user.createdAt = now;
        user.updatedAt = now;
        return user;
    }

    public void updateDisplayName(@NonNull final String displayName) {
        this.displayName = displayName;
        this.updatedAt = LocalDateTime.now();
    }
}
