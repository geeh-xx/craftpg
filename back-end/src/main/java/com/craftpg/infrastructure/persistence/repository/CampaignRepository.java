package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.campaign.Campaign;
import com.craftpg.domain.model.campaign.CampaignID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, CampaignID> {

    @Query("""
            select distinct c
            from Campaign c
            inner join CampaignRole cr on cr.id.campaignId = c.id.value
            where cr.id.userId = :userId
            """)
    Page<Campaign> findPageByUserId(@Param("userId") UUID userId, Pageable pageable);
}
