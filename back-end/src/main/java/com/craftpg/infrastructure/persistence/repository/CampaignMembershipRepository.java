package com.craftpg.infrastructure.persistence.repository;

import lombok.NonNull;

import com.craftpg.domain.model.CampaignMembership;
import com.craftpg.domain.model.CampaignMembershipId;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignMembershipRepository extends JpaRepository<CampaignMembership, CampaignMembershipId> {

    boolean existsByIdCampaignIdAndIdUserId(@NonNull final UUID campaignId, @NonNull final UUID userId);
}
