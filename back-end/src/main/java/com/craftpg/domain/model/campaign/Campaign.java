package com.craftpg.domain.model.campaign;

import com.craftpg.domain.AggregateRoot;
import com.craftpg.domain.input.CreateCampaignInput;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "campaign")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Campaign extends AggregateRoot<CampaignID> {

    @EmbeddedId
    private CampaignID id;

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

    @Column(name = "create_by", nullable = false)
    private UUID createBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static Campaign create(final CreateCampaignInput input) {
        var campaign = new Campaign();
        var now = LocalDateTime.now();
        campaign.id = CampaignID.generate();
        campaign.title = input.title();
        campaign.description = input.description();
        campaign.system = "Tormenta20";
        campaign.frequency = input.frequency();
        campaign.status = input.status();
        campaign.progressPercent = input.progressPercent();
        campaign.createBy = input.createBy();
        campaign.createdAt = now;
        campaign.updatedAt = now;
        return campaign;
    }

    public void update(final String title, final String description, final String frequency, final String status, final Integer progressPercent) {
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
