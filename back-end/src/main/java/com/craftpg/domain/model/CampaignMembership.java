package com.craftpg.domain.model;

import org.jspecify.annotations.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campaign_membership")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignMembership {

    @EmbeddedId
    private CampaignMembershipId id;

    @Column(name = "campaign_character_id", nullable = false)
    private UUID campaignCharacterId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static CampaignMembership create(@NonNull final UUID campaignId, @NonNull final UUID userId, @NonNull final UUID campaignCharacterId) {
        var membership = new CampaignMembership();
        membership.id = new CampaignMembershipId(campaignId, userId);
        membership.campaignCharacterId = campaignCharacterId;
        membership.createdAt = LocalDateTime.now();
        return membership;
    }
}
