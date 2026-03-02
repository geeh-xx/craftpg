package com.craftpg.application.usecase.character.getcampaigncharacter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignCharacterRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCampaignCharacterUsecaseImplTest {

    @Mock
    private CampaignCharacterRepository campaignCharacterRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private GetCampaignCharacterUsecaseImpl usecase;

    @Test
    void execute_existingCharacterForUser_returnsCharacter() {
        // Given
        var campaignId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var character = mock(CampaignCharacter.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignCharacterRepository.findByCampaignIdAndUserId(campaignId, userId)).thenReturn(Optional.of(character));

        // When
        var result = usecase.execute(campaignId);

        // Then
        assertSame(character, result);
        verify(campaignCharacterRepository).findByCampaignIdAndUserId(campaignId, userId);
    }

    @Test
    void execute_missingCharacter_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        var userId = UUID.randomUUID();

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignCharacterRepository.findByCampaignIdAndUserId(campaignId, userId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId));

        // Then
        assertEquals("campaign character not found", exception.getMessage());
    }
}
