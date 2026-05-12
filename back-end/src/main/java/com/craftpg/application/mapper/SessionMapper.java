package com.craftpg.application.mapper;

import com.craftpg.domain.model.campaign.CampaignSession;
import com.craftpg.infrastructure.web.dto.SessionResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionResponse toResponse(@NonNull final CampaignSession session) {
        return new SessionResponse(
                session.getId(),
                session.getCampaignId(),
                session.getTitle(),
                session.getScheduledAt()
        )
                .summary(session.getSummary())
                .notes(session.getNotes());
    }
}
