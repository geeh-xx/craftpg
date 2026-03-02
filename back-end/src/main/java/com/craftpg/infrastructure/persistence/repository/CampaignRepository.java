package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.Campaign;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
}
