package com.craftpg.domain.model;

import com.craftpg.domain.input.CreateCampaignInput;
import lombok.NonNull;
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
@Table(name = "campaign")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Campaign {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String system;

    @Column(nullable = false)
    private String frequency;

    @Column(nullable = false)
    private String status;

    @Column(name = "progress_percent", nullable = false)
    private Integer progressPercent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static Campaign create(@NonNull final CreateCampaignInput input) {
        var campaign = new Campaign();
        var now = LocalDateTime.now();
        campaign.id = UUID.randomUUID();
        campaign.title = input.title();
        campaign.description = input.description();
        campaign.system = "Tormenta20";
        campaign.frequency = input.frequency();
        campaign.status = input.status();
        campaign.progressPercent = input.progressPercent();
        campaign.createdAt = now;
        campaign.updatedAt = now;
        return campaign;
    }

    public void update(@NonNull final String title, @NonNull final String description, @NonNull final String frequency, @NonNull final String status, @NonNull final Integer progressPercent) {
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.status = status;
        this.progressPercent = progressPercent;
        this.updatedAt = LocalDateTime.now();
    }

    public void finish() {
        this.status = "finished";
        this.progressPercent = 100;
        this.updatedAt = LocalDateTime.now();
    }
}
