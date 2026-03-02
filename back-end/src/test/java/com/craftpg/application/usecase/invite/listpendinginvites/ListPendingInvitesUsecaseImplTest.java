package com.craftpg.application.usecase.invite.listpendinginvites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListPendingInvitesUsecaseImplTest {

    @Mock
    private CampaignInviteRepository campaignInviteRepository;

    @InjectMocks
    private ListPendingInvitesUsecaseImpl usecase;

    @Test
    void execute_campaignId_returnsPendingInvites() {
        // Given
        var campaignId = UUID.randomUUID();
        var invites = List.of(mock(CampaignInvite.class));

        when(campaignInviteRepository.findAllByCampaignIdAndAcceptedAtIsNullOrderByCreatedAtDesc(campaignId)).thenReturn(invites);

        // When
        var result = usecase.execute(campaignId);

        // Then
        assertEquals(1, result.size());
        verify(campaignInviteRepository).findAllByCampaignIdAndAcceptedAtIsNullOrderByCreatedAtDesc(campaignId);
    }
}
