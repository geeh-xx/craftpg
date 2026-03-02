package com.craftpg.domain.model;

import lombok.NonNull;

import com.craftpg.shared.constants.CampaignRoleType;
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
@Table(name = "campaign_role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignRole {

    @EmbeddedId
    private CampaignRoleId id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static CampaignRole create(@NonNull final UUID campaignId, @NonNull final UUID userId, @NonNull final CampaignRoleType roleType) {
        var role = new CampaignRole();
        role.id = new CampaignRoleId(campaignId, userId, roleType.name());
        role.createdAt = LocalDateTime.now();
        return role;
    }
}
