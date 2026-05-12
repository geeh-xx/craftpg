package com.craftpg.domain.model.campaign;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
public class CampaignRoleId implements Serializable {

    @Column(name = "campaign_id")
    private UUID campaignId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "role")
    private String role;

    public CampaignRoleId() {
    }

    public CampaignRoleId(final UUID campaignId, final UUID userId, final String role) {
        this.campaignId = campaignId;
        this.userId = userId;
        this.role = role;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CampaignRoleId that = (CampaignRoleId) o;
        return Objects.equals(campaignId, that.campaignId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(campaignId, userId, role);
    }
}
