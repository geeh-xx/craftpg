package com.craftpg.application.usecase.campaign.finishcampaign;

import com.craftpg.application.mapper.CampaignMapper;
import com.craftpg.domain.model.campaign.Campaign;
import com.craftpg.domain.model.campaign.CampaignID;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinishCampaignUsecaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignMapper campaignMapper;

    @InjectMocks
    private FinishCampaignUsecaseImpl usecase;

    @Test
    void execute_existingCampaign_finishesAndSavesCampaign() {
        // Given
        var campaignId = UUID.randomUUID();
        var campaign = mock(Campaign.class);
        var response = mock(CampaignResponse.class);
        when(campaignRepository.findById(CampaignID.of(campaignId))).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(campaignMapper.toResponse(campaign)).thenReturn(response);

        // When
        var result = usecase.execute(campaignId);

        // Then
        assertEquals(response, result.getValue().orElseThrow());
        verify(campaign).finish();
        verify(campaignRepository).save(campaign);
        verify(campaignMapper).toResponse(campaign);
    }

    @Test
    void execute_missingCampaign_returnsErrorResult() {
        // Given
        var campaignId = UUID.randomUUID();
        when(campaignRepository.findById(CampaignID.of(campaignId))).thenReturn(Optional.empty());

        // When
        var result = usecase.execute(campaignId);

        // Then
        assertEquals("campaign not found", result.getMessage());
    }
}
