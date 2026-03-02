package com.craftpg.application.usecase.session.updatesession;

import com.craftpg.domain.model.CampaignSession;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignSessionRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import com.craftpg.infrastructure.web.dto.UpdateSessionRequest;
import lombok.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateSessionUsecaseImpl implements UpdateSessionUsecase {

    private final CampaignSessionRepository campaignSessionRepository;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.EDIT_SESSION)
    public CampaignSession execute(@NonNull final UUID campaignId, @NonNull final UUID sessionId, @NonNull final UpdateSessionRequest command) {
        var session = campaignSessionRepository.findByCampaignIdAndId(campaignId, sessionId)
            .orElseThrow(() -> new ApiException("session not found"));

        session.update(
            command.getTitle(),
            command.getScheduledAt(),
            command.getSummary() == null ? "" : command.getSummary(),
            command.getNotes() == null ? "" : command.getNotes(),
            command.getAttendanceJson() == null ? "[]" : command.getAttendanceJson(),
            command.getXpJson() == null ? "[]" : command.getXpJson(),
            command.getNpcsJson() == null ? "[]" : command.getNpcsJson(),
            command.getMapsJson() == null ? "[]" : command.getMapsJson(),
            command.getTreasuresJson() == null ? "[]" : command.getTreasuresJson()
        );

        return campaignSessionRepository.save(session);
    }
}
