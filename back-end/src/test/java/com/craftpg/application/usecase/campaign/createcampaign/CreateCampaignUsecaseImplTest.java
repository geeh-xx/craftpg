package com.craftpg.application.usecase.campaign.createcampaign;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.application.mapper.CampaignCreateInputMapper;
import com.craftpg.domain.input.CreateCampaignInput;
import com.craftpg.domain.model.Campaign;
import com.craftpg.domain.model.CampaignRole;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCampaignUsecaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignRoleRepository campaignRoleRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private CampaignCreateInputMapper campaignCreateInputMapper;

    @InjectMocks
    private CreateCampaignUsecaseImpl usecase;

    @Test
    void execute_validCommand_returnsSavedCampaign() {
        // Given
        var command = Instancio.create(CreateCampaignRequest.class);
        var userId = UUID.randomUUID();
        var createInput = Instancio.create(CreateCampaignInput.class);
        var campaign = Campaign.create(createInput);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignCreateInputMapper.toCreateInput(command)).thenReturn(createInput);
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        // When
        var result = usecase.execute(command);

        // Then
        assertSame(campaign, result);
        verify(campaignRepository).save(any(Campaign.class));
        verify(campaignRoleRepository).save(any(CampaignRole.class));
    }
}
