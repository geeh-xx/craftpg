package com.craftpg.application.usecase.campaign.getcampaign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCampaignUsecaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private GetCampaignUsecaseImpl usecase;

    @Test
    void execute_existingCampaign_returnsCampaign() {
        // Given
        var campaignId = UUID.randomUUID();
        var campaign = mock(Campaign.class);
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        // When
        var result = usecase.execute(campaignId);

        // Then
        assertSame(campaign, result);
        verify(campaignRepository).findById(campaignId);
    }

    @Test
    void execute_missingCampaign_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId));

        // Then
        assertEquals("campaign not found", exception.getMessage());
    }
}
