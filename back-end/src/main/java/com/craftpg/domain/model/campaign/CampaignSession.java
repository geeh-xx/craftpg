package com.craftpg.domain.model.campaign;

import com.craftpg.domain.input.CreateCampaignSessionInput;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "campaign_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignSession {

    @Id
    private UUID id;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(nullable = false)
    private String title;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(columnDefinition = "text")
    private String notes;

    @Column(name = "attendance_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String attendanceJson;

    @Column(name = "xp_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String xpJson;

    @Column(name = "npcs_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String npcsJson;

    @Column(name = "maps_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String mapsJson;

    @Column(name = "treasures_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String treasuresJson;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static CampaignSession create(final CreateCampaignSessionInput input) {
        var session = new CampaignSession();
        session.id = UUID.randomUUID();
        session.campaignId = input.campaignId();
        session.title = input.title();
        session.scheduledAt = input.scheduledAt();
        session.summary = input.summary();
        session.notes = input.notes();
        session.attendanceJson = input.attendanceJson();
        session.xpJson = input.xpJson();
        session.npcsJson = input.npcsJson();
        session.mapsJson = input.mapsJson();
        session.treasuresJson = input.treasuresJson();
        session.createdAt = LocalDateTime.now();
        return session;
    }

    public void update(final String title, final LocalDateTime scheduledAt, final String summary, final String notes, final String attendanceJson, final String xpJson, final String npcsJson, final String mapsJson, final String treasuresJson
    ) {
        this.title = title;
        this.scheduledAt = scheduledAt;
        this.summary = summary;
        this.notes = notes;
        this.attendanceJson = attendanceJson;
        this.xpJson = xpJson;
        this.npcsJson = npcsJson;
        this.mapsJson = mapsJson;
        this.treasuresJson = treasuresJson;
    }
}
