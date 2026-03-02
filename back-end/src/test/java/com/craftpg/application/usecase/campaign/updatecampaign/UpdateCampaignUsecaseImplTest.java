package com.craftpg.application.usecase.campaign.updatecampaign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import java.util.Optional;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCampaignUsecaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private UpdateCampaignUsecaseImpl usecase;

    @Test
    void execute_existingCampaign_updatesAndSavesCampaign() {
        // Given
        var campaignId = UUID.randomUUID();
        var command = mock(UpdateCampaignRequest.class);
        var campaign = mock(Campaign.class);

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(campaign)).thenReturn(campaign);

        // When
        var result = usecase.execute(campaignId, command);

        // Then
        assertSame(campaign, result);
        verify(campaign).update(
            command.getTitle(),
            command.getDescription(),
            command.getFrequency(),
            command.getStatus(),
            command.getProgressPercent()
        );
        verify(campaignRepository).save(campaign);
    }

    @Test
    void execute_missingCampaign_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        var command = Instancio.create(UpdateCampaignRequest.class);
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId, command));

        // Then
        assertEquals("campaign not found", exception.getMessage());
    }
}
