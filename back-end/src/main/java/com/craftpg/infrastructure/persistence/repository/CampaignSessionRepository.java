package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.CampaignSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignSessionRepository extends JpaRepository<CampaignSession, UUID> {
	List<CampaignSession> findAllByCampaignIdOrderByScheduledAtAsc(UUID campaignId);

	Optional<CampaignSession> findByCampaignIdAndId(UUID campaignId, UUID id);
}
