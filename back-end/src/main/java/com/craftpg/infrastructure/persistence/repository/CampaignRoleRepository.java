package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.campaign.CampaignRole;
import com.craftpg.domain.model.campaign.CampaignRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignRoleRepository extends JpaRepository<CampaignRole, CampaignRoleId> {

    List<CampaignRole> findByIdUserId(final UUID userId);

    boolean existsByIdCampaignIdAndIdUserIdAndIdRole(final UUID campaignId, final UUID userId, final String role);

    Optional<CampaignRole> findFirstByIdCampaignIdAndIdRole(UUID campaignId, String role);
}
