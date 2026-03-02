package com.craftpg.infrastructure.persistence.repository;

import lombok.NonNull;

import com.craftpg.domain.model.CampaignInvite;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignInviteRepository extends JpaRepository<CampaignInvite, UUID> {

    Optional<CampaignInvite> findByTokenHash(@NonNull final String tokenHash);

    List<CampaignInvite> findAllByCampaignIdAndAcceptedAtIsNullOrderByCreatedAtDesc(@NonNull UUID campaignId);

    Optional<CampaignInvite> findByIdAndCampaignId(@NonNull UUID id, @NonNull UUID campaignId);
}
