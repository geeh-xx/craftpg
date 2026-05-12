package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.campaign.CampaignSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignSessionRepository extends JpaRepository<CampaignSession, UUID> {
    List<CampaignSession> findAllByCampaignIdOrderByScheduledAtAsc(UUID campaignId);

    Optional<CampaignSession> findByCampaignIdAndId(UUID campaignId, UUID id);
}
