package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.mapper.SessionMapper;
import com.craftpg.application.usecase.session.createsession.CreateSessionUsecase;
import com.craftpg.application.usecase.session.listsession.ListSessionUsecase;
import com.craftpg.application.usecase.session.updatesession.UpdateSessionUsecase;
import com.craftpg.infrastructure.factory.UseCaseProvider;
import com.craftpg.infrastructure.web.api.SessionsApi;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;
import com.craftpg.infrastructure.web.dto.SessionResponse;
import com.craftpg.infrastructure.web.dto.UpdateSessionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SessionController implements SessionsApi {

    private final UseCaseProvider useCaseProvider;
    private final SessionMapper sessionMapper;

    @Override
    public ResponseEntity<List<SessionResponse>> getSessionsByCampaignId(final UUID campaignId) {
        return ResponseEntity.ok(useCaseProvider.getUseCase(ListSessionUsecase.class)
                .execute(campaignId)
                .stream()
                .map(sessionMapper::toResponse)
                .toList());
    }

    @Override
    public ResponseEntity<SessionResponse> createSessionByCampaignId(final UUID campaignId, final CreateSessionRequest createSessionRequest) {
        var session = useCaseProvider.getUseCase(CreateSessionUsecase.class).execute(campaignId, createSessionRequest);
        return ResponseEntity.status(201).body(sessionMapper.toResponse(session));
    }

    @Override
    public ResponseEntity<SessionResponse> updateSessionByCampaignIdAndSessionId(final UUID campaignId, final UUID sessionId, final UpdateSessionRequest updateSessionRequest
    ) {
        var session = useCaseProvider.getUseCase(UpdateSessionUsecase.class).execute(campaignId, sessionId, updateSessionRequest);
        return ResponseEntity.ok(sessionMapper.toResponse(session));
    }
}
