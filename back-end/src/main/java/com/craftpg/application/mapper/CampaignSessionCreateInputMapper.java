package com.craftpg.application.mapper;

import com.craftpg.domain.input.CreateCampaignSessionInput;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CampaignSessionCreateInputMapper {

    public CreateCampaignSessionInput toCreateInput(final UUID campaignId, final CreateSessionRequest request) {
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
