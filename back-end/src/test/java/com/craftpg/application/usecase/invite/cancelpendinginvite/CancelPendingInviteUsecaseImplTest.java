package com.craftpg.application.usecase.invite.cancelpendinginvite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelPendingInviteUsecaseImplTest {

    @Mock
    private CampaignInviteRepository campaignInviteRepository;

    @InjectMocks
    private CancelPendingInviteUsecaseImpl usecase;

    @Test
    void execute_pendingInvite_deletesInvite() {
        // Given
        var campaignId = UUID.randomUUID();
        var inviteId = UUID.randomUUID();
        var invite = mock(CampaignInvite.class);

        when(campaignInviteRepository.findByIdAndCampaignId(inviteId, campaignId)).thenReturn(Optional.of(invite));
        when(invite.isAccepted()).thenReturn(false);

        // When
        usecase.execute(campaignId, inviteId);

        // Then
        verify(campaignInviteRepository).delete(invite);
    }

    @Test
    void execute_inviteNotFound_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        var inviteId = UUID.randomUUID();

        when(campaignInviteRepository.findByIdAndCampaignId(inviteId, campaignId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId, inviteId));

        // Then
        assertEquals("invite not found", exception.getMessage());
    }

    @Test
    void execute_alreadyAcceptedInvite_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        var inviteId = UUID.randomUUID();
        var invite = mock(CampaignInvite.class);

        when(campaignInviteRepository.findByIdAndCampaignId(inviteId, campaignId)).thenReturn(Optional.of(invite));
        when(invite.isAccepted()).thenReturn(true);

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId, inviteId));

        // Then
        assertEquals("invite already accepted", exception.getMessage());
    }
}
