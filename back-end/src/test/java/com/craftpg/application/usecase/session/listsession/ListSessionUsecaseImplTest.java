package com.craftpg.application.usecase.session.listsession;

import com.craftpg.domain.model.campaign.CampaignSession;
import com.craftpg.infrastructure.persistence.repository.CampaignSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListSessionUsecaseImplTest {

    @Mock
    private CampaignSessionRepository campaignSessionRepository;

    @InjectMocks
    private ListSessionUsecaseImpl usecase;

    @Test
    void execute_campaignId_returnsSessionsOrderedFromRepository() {
        // Given
        var campaignId = UUID.randomUUID();
        var sessions = List.of(mock(CampaignSession.class));

        when(campaignSessionRepository.findAllByCampaignIdOrderByScheduledAtAsc(campaignId)).thenReturn(sessions);

        // When
        var result = usecase.execute(campaignId);

        // Then
        assertEquals(1, result.size());
        verify(campaignSessionRepository).findAllByCampaignIdOrderByScheduledAtAsc(campaignId);
    }
}
