package com.craftpg.application.usecase.campaign.deletecampaign;

import static org.mockito.Mockito.verify;

import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteCampaignUsecaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private DeleteCampaignUsecaseImpl usecase;

    @Test
    void execute_validCampaignId_deletesCampaignById() {
        // Given
        var campaignId = UUID.randomUUID();

        // When
        usecase.execute(campaignId);

        // Then
        verify(campaignRepository).deleteById(campaignId);
    }
}
