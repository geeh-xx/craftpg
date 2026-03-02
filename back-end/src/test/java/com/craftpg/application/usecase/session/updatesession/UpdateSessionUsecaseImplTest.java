package com.craftpg.application.usecase.session.updatesession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CampaignSession;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignSessionRepository;
import com.craftpg.infrastructure.web.dto.UpdateSessionRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateSessionUsecaseImplTest {

    @Mock
    private CampaignSessionRepository campaignSessionRepository;

    @InjectMocks
    private UpdateSessionUsecaseImpl usecase;

    @Test
    void execute_existingSession_updatesWithDefaultsAndSaves() {
        // Given
        var campaignId = UUID.randomUUID();
        var sessionId = UUID.randomUUID();
        var command = mock(UpdateSessionRequest.class);
        var session = mock(CampaignSession.class);

        when(command.getTitle()).thenReturn("Session title");
        when(command.getScheduledAt()).thenReturn(LocalDateTime.now());
        when(command.getSummary()).thenReturn(null);
        when(command.getNotes()).thenReturn(null);
        when(command.getAttendanceJson()).thenReturn(null);
        when(command.getXpJson()).thenReturn(null);
        when(command.getNpcsJson()).thenReturn(null);
        when(command.getMapsJson()).thenReturn(null);
        when(command.getTreasuresJson()).thenReturn(null);
        when(campaignSessionRepository.findByCampaignIdAndId(campaignId, sessionId)).thenReturn(Optional.of(session));
        when(campaignSessionRepository.save(session)).thenReturn(session);

        // When
        var result = usecase.execute(campaignId, sessionId, command);

        // Then
        assertSame(session, result);
        verify(session).update(
            "Session title",
            command.getScheduledAt(),
            "",
            "",
            "[]",
            "[]",
            "[]",
            "[]",
            "[]"
        );
        verify(campaignSessionRepository).save(session);
    }

    @Test
    void execute_sessionNotFound_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        var sessionId = UUID.randomUUID();
        var command = Instancio.create(UpdateSessionRequest.class);

        when(campaignSessionRepository.findByCampaignIdAndId(campaignId, sessionId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId, sessionId, command));

        // Then
        assertEquals("session not found", exception.getMessage());
    }
}
