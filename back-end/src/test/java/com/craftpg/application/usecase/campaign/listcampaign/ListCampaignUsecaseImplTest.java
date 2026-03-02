package com.craftpg.application.usecase.campaign.listcampaign;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.Campaign;
import com.craftpg.domain.model.CampaignRole;
import com.craftpg.domain.model.CampaignRoleId;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListCampaignUsecaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignRoleRepository campaignRoleRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ListCampaignUsecaseImpl usecase;

    @Test
    void execute_userWithRoles_returnsCampaignsFromRoleCampaignIds() {
        // Given
        var userId = UUID.randomUUID();
        var campaignId = UUID.randomUUID();
        var role = mock(CampaignRole.class);
        var roleId = mock(CampaignRoleId.class);
        var campaign = mock(Campaign.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignRoleRepository.findByIdUserId(userId)).thenReturn(List.of(role));
        when(role.getId()).thenReturn(roleId);
        when(roleId.getCampaignId()).thenReturn(campaignId);
        when(campaignRepository.findAllById(List.of(campaignId))).thenReturn(List.of(campaign));

        // When
        var result = usecase.execute();

        // Then
        assertSame(campaign, result.getFirst());
        verify(campaignRepository).findAllById(List.of(campaignId));
    }
}
