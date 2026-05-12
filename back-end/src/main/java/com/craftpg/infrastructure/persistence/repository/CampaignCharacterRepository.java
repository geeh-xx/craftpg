package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.campaign.CampaignCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CampaignCharacterRepository extends JpaRepository<CampaignCharacter, UUID> {

    Optional<CampaignCharacter> findByCampaignIdAndId(UUID campaignId, UUID id);

    Optional<CampaignCharacter> findByCampaignIdAndUserId(UUID campaignId, UUID userId);
}
