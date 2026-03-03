package com.craftpg.application.mapper;

import com.craftpg.domain.input.CreateCampaignInput;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CampaignCreateInputMapper {

    @NonNull
    public CreateCampaignInput toCreateInput(@NonNull final CreateCampaignRequest request) {
        return new CreateCampaignInput(
            request.getTitle(),
            request.getDescription(),
            request.getFrequency(),
            request.getStatus(),
            request.getProgressPercent()
        );
    }
}
