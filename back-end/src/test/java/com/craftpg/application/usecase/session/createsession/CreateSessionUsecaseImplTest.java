package com.craftpg.application.usecase.session.createsession;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.application.mapper.CampaignSessionCreateInputMapper;
import com.craftpg.domain.input.CreateCampaignSessionInput;
import com.craftpg.domain.model.CampaignSession;
import com.craftpg.infrastructure.persistence.repository.CampaignSessionRepository;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateSessionUsecaseImplTest {

    @Mock
    private CampaignSessionRepository campaignSessionRepository;

    @Mock
    private CampaignSessionCreateInputMapper campaignSessionCreateInputMapper;

    @InjectMocks
    private CreateSessionUsecaseImpl usecase;

    @Test
    void execute_validCommand_createsAndSavesSession() {
        // Given
        var campaignId = UUID.randomUUID();
        var command = Instancio.create(CreateSessionRequest.class);
        var input = Instancio.create(CreateCampaignSessionInput.class);
        var savedSession = mock(CampaignSession.class);

        when(campaignSessionCreateInputMapper.toCreateInput(campaignId, command)).thenReturn(input);
        when(campaignSessionRepository.save(any(CampaignSession.class))).thenReturn(savedSession);

        // When
        var result = usecase.execute(campaignId, command);

        // Then
        assertSame(savedSession, result);
        verify(campaignSessionCreateInputMapper).toCreateInput(campaignId, command);
        verify(campaignSessionRepository).save(any(CampaignSession.class));
    }
}
