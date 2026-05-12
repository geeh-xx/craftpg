package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.campaign.CampaignInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignInviteRepository extends JpaRepository<CampaignInvite, UUID> {

    Optional<CampaignInvite> findByTokenHash(final String tokenHash);

    List<CampaignInvite> findAllByCampaignIdAndAcceptedAtIsNullOrderByCreatedAtDesc(UUID campaignId);

    Optional<CampaignInvite> findByIdAndCampaignId(UUID id, UUID campaignId);
}
