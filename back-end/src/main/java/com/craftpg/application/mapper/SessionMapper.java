package com.craftpg.application.mapper;

import com.craftpg.domain.model.CampaignSession;
import com.craftpg.infrastructure.web.dto.SessionResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    @NonNull
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
