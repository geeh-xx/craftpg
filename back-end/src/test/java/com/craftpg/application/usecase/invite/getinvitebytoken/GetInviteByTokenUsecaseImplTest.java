package com.craftpg.application.usecase.invite.getinvitebytoken;

import com.craftpg.domain.model.campaign.Campaign;
import com.craftpg.domain.model.campaign.CampaignID;
import com.craftpg.domain.model.campaign.CampaignInvite;
import com.craftpg.domain.model.campaign.CampaignRole;
import com.craftpg.domain.model.campaign.CampaignRoleId;
import com.craftpg.domain.model.user.AppUser;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.shared.util.HashUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetInviteByTokenUsecaseImplTest {

    @Mock
    private CampaignInviteRepository campaignInviteRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignRoleRepository campaignRoleRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private GetInviteByTokenUsecaseImpl usecase;

    @Test
    void execute_existingToken_returnsInvite() {
        // Given
        var token = "token-value";
        var invite = mock(CampaignInvite.class);
        var campaign = mock(Campaign.class);
        var dmRole = mock(CampaignRole.class);
        var dmUser = mock(AppUser.class);
        var campaignId = UUID.randomUUID();
        var dmUserId = UUID.randomUUID();
        var dmRoleId = new CampaignRoleId(campaignId, dmUserId, "DM");

        when(invite.getCampaignId()).thenReturn(campaignId);
        when(campaign.getTitle()).thenReturn("Tormenta campaign");
        when(dmRole.getId()).thenReturn(dmRoleId);
        when(dmUser.getDisplayName()).thenReturn("Dungeon Master");

        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.of(invite));
        when(campaignRepository.findById(CampaignID.of(campaignId))).thenReturn(Optional.of(campaign));
        when(campaignRoleRepository.findFirstByIdCampaignIdAndIdRole(campaignId, "DM")).thenReturn(Optional.of(dmRole));
        when(appUserRepository.findById(dmUserId)).thenReturn(Optional.of(dmUser));

        // When
        var result = usecase.execute(token);

        // Then
        assertEquals(invite, result.invite());
        assertEquals("Tormenta campaign", result.campaignTitle());
        assertEquals("Dungeon Master", result.dmName());
        verify(campaignInviteRepository).findByTokenHash(HashUtil.sha256(token));
    }

    @Test
    void execute_missingToken_throwsApiException() {
        // Given
        var token = "token-value";
        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(token));

        // Then
        assertEquals("invite not found", exception.getMessage());
    }
}
