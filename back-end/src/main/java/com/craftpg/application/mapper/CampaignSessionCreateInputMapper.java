package com.craftpg.application.mapper;

import com.craftpg.domain.input.CreateCampaignSessionInput;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;
import lombok.NonNull;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CampaignSessionCreateInputMapper {

    @NonNull
    public CreateCampaignSessionInput toCreateInput(@NonNull final UUID campaignId, @NonNull final CreateSessionRequest request) {
        return new CreateCampaignSessionInput(
            campaignId,
            request.getTitle(),
            request.getScheduledAt(),
            request.getSummary() == null ? "" : request.getSummary(),
            request.getNotes() == null ? "" : request.getNotes(),
            request.getAttendanceJson() == null ? "[]" : request.getAttendanceJson(),
            request.getXpJson() == null ? "[]" : request.getXpJson(),
            request.getNpcsJson() == null ? "[]" : request.getNpcsJson(),
            request.getMapsJson() == null ? "[]" : request.getMapsJson(),
            request.getTreasuresJson() == null ? "[]" : request.getTreasuresJson()
        );
    }
}
