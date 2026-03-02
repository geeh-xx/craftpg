package com.craftpg.infrastructure.web.controller;

import lombok.NonNull;

import com.craftpg.application.mapper.SessionMapper;
import com.craftpg.application.usecase.session.createsession.CreateSessionUsecase;
import com.craftpg.application.usecase.session.listsession.ListSessionUsecase;
import com.craftpg.application.usecase.session.updatesession.UpdateSessionUsecase;
import com.craftpg.infrastructure.web.api.SessionsApi;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;
import com.craftpg.infrastructure.web.dto.SessionResponse;
import com.craftpg.infrastructure.web.dto.UpdateSessionRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionController implements SessionsApi {

    private final CreateSessionUsecase createSessionUsecase;
    private final ListSessionUsecase listSessionUsecase;
    private final UpdateSessionUsecase updateSessionUsecase;
    private final SessionMapper sessionMapper;

    @Override
    public ResponseEntity<List<SessionResponse>> campaignsCampaignIdSessionsGet(@NonNull final UUID campaignId) {
        return ResponseEntity.ok(listSessionUsecase.execute(campaignId).stream().map(sessionMapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<SessionResponse> campaignsCampaignIdSessionsPost(@NonNull final UUID campaignId, @NonNull final CreateSessionRequest createSessionRequest) {
        var session = createSessionUsecase.execute(campaignId, createSessionRequest);
        return ResponseEntity.status(201).body(sessionMapper.toResponse(session));
    }

    @Override
    public ResponseEntity<SessionResponse> campaignsCampaignIdSessionsSessionIdPatch(
        @NonNull final UUID campaignId,
        @NonNull final UUID sessionId,
        @NonNull final UpdateSessionRequest updateSessionRequest
    ) {
        var session = updateSessionUsecase.execute(campaignId, sessionId, updateSessionRequest);
        return ResponseEntity.ok(sessionMapper.toResponse(session));
    }
}
