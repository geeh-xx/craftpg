package com.craftpg.application.usecase.invite.getinvitebytoken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.AppUser;
import com.craftpg.domain.model.Campaign;
import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.domain.model.CampaignRole;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.shared.util.HashUtil;
import java.util.UUID;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        var dmRoleId = new com.craftpg.domain.model.CampaignRoleId(campaignId, dmUserId, "DM");

        when(invite.getCampaignId()).thenReturn(campaignId);
        when(campaign.getTitle()).thenReturn("Tormenta campaign");
        when(dmRole.getId()).thenReturn(dmRoleId);
        when(dmUser.getDisplayName()).thenReturn("Dungeon Master");

        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.of(invite));
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
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
