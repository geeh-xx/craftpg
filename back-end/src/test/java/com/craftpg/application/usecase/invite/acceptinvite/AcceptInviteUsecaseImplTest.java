package com.craftpg.application.usecase.invite.acceptinvite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignCharacterRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignMembershipRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.shared.util.HashUtil;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcceptInviteUsecaseImplTest {

    @Mock
    private CampaignInviteRepository campaignInviteRepository;

    @Mock
    private CampaignRoleRepository campaignRoleRepository;

    @Mock
    private CampaignMembershipRepository campaignMembershipRepository;

    @Mock
    private CharacterBaseRepository characterBaseRepository;

    @Mock
    private CampaignCharacterRepository campaignCharacterRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private AcceptInviteUsecaseImpl usecase;

    @Test
    void execute_validInput_acceptsInviteAndReturnsCampaignId() {
        // Given
        var token = "token";
        var campaignId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var characterBaseId = UUID.randomUUID();

        var invite = mock(CampaignInvite.class);
        var characterBase = mock(CharacterBase.class);
        var campaignCharacter = mock(CampaignCharacter.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.of(invite));
        when(invite.isAccepted()).thenReturn(false);
        when(invite.isExpired()).thenReturn(false);
        when(invite.getCampaignId()).thenReturn(campaignId);
        when(invite.getRolesJson()).thenReturn("[\"PLAYER\",\"DM\"]");

        when(campaignMembershipRepository.existsByIdCampaignIdAndIdUserId(campaignId, userId)).thenReturn(false);
        when(characterBaseRepository.findById(characterBaseId)).thenReturn(Optional.of(characterBase));
        when(characterBase.getOwnerUserId()).thenReturn(userId);

        when(campaignCharacterRepository.save(any(CampaignCharacter.class))).thenReturn(campaignCharacter);
        when(campaignCharacter.getId()).thenReturn(UUID.randomUUID());

        // When
        var result = usecase.execute(token, characterBaseId);

        // Then
        assertEquals(campaignId, result);
        verify(campaignMembershipRepository).save(any());
        verify(campaignRoleRepository, times(1)).save(any());
        verify(invite).accept(userId);
        verify(campaignInviteRepository).save(invite);
    }

    @Test
    void execute_inviteNotFound_throwsApiException() {
        // Given
        var token = "token";
        var characterBaseId = UUID.randomUUID();
        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(token, characterBaseId));

        // Then
        assertEquals("invite not found", exception.getMessage());
    }

    @Test
    void execute_alreadyAcceptedInvite_throwsApiException() {
        // Given
        var token = "token";
        var characterBaseId = UUID.randomUUID();
        var invite = mock(CampaignInvite.class);

        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.of(invite));
        when(invite.isAccepted()).thenReturn(true);

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(token, characterBaseId));

        // Then
        assertEquals("invite already accepted", exception.getMessage());
    }

    @Test
    void execute_expiredInvite_throwsApiException() {
        // Given
        var token = "token";
        var characterBaseId = UUID.randomUUID();
        var invite = mock(CampaignInvite.class);

        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.of(invite));
        when(invite.isAccepted()).thenReturn(false);
        when(invite.isExpired()).thenReturn(true);

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(token, characterBaseId));

        // Then
        assertEquals("invite expired", exception.getMessage());
    }
}
