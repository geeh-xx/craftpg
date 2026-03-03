package com.craftpg.infrastructure.persistence.repository;

import org.jspecify.annotations.NonNull;

import com.craftpg.domain.model.CampaignRole;
import com.craftpg.domain.model.CampaignRoleId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRoleRepository extends JpaRepository<CampaignRole, CampaignRoleId> {

    List<CampaignRole> findByIdUserId(@NonNull final UUID userId);

    boolean existsByIdCampaignIdAndIdUserIdAndIdRole(@NonNull final UUID campaignId, @NonNull final UUID userId, @NonNull final String role);

    Optional<CampaignRole> findFirstByIdCampaignIdAndIdRole(@NonNull UUID campaignId, @NonNull String role);
}
