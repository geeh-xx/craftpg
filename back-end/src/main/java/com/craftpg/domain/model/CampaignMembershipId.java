package com.craftpg.domain.model;

import lombok.Getter;
import lombok.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Embeddable
public class CampaignMembershipId implements Serializable {

    @Column(name = "campaign_id")
    private UUID campaignId;

    @Column(name = "user_id")
    private UUID userId;

    public CampaignMembershipId() {
    }

    public CampaignMembershipId(@NonNull final UUID campaignId, @NonNull final UUID userId) {
        this.campaignId = campaignId;
        this.userId = userId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CampaignMembershipId that = (CampaignMembershipId) o;
        return Objects.equals(campaignId, that.campaignId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(campaignId, userId);
    }
}
