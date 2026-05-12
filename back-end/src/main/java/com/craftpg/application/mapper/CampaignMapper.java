package com.craftpg.application.mapper;

import com.craftpg.domain.input.CreateCampaignInput;
import com.craftpg.domain.model.campaign.Campaign;
import com.craftpg.infrastructure.web.dto.CampaignPageResponse;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CampaignMapper {

    public CreateCampaignInput toCreateInput(@NonNull final CreateCampaignRequest request, @NonNull final UUID userId) {
        return new CreateCampaignInput(
                userId,
                request.getTitle(),
                request.getDescription(),
                request.getFrequency(),
                request.getStatus(),
                request.getProgressPercent()
        );
    }

    public CampaignResponse toResponse(@NonNull final Campaign campaign) {
        return new CampaignResponse(
                campaign.getId().getValue(),
                campaign.getTitle(),
                campaign.getSystem(),
                campaign.getFrequency(),
                campaign.getStatus(),
                campaign.getProgressPercent()
        ).description(campaign.getDescription());
    }

    public CampaignPageResponse toPageResponse(@NonNull final Page<CampaignResponse> page) {
        var response = new CampaignPageResponse();
        response.setContent(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setHasNext(page.hasNext());
        return response;
    }
}
