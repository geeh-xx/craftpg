package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.campaign.CampaignMembership;
import com.craftpg.domain.model.campaign.CampaignMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CampaignMembershipRepository extends JpaRepository<CampaignMembership, CampaignMembershipId> {

    boolean existsByIdCampaignIdAndIdUserId(final UUID campaignId, final UUID userId);
}
