package com.craftpg.application.usecase.character.updatecampaigncharacter;

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
import com.craftpg.infrastructure.web.dto.UpdateCampaignCharacterRequest;
import java.util.Optional;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCampaignCharacterUsecaseImplTest {

    @Mock
    private CampaignCharacterRepository campaignCharacterRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private UpdateCampaignCharacterUsecaseImpl usecase;

    @Test
    void execute_existingCharacter_updatesDefaultsAndSaves() {
        // Given
        var campaignId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var command = mock(UpdateCampaignCharacterRequest.class);
        var character = mock(CampaignCharacter.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignCharacterRepository.findByCampaignIdAndUserId(campaignId, userId)).thenReturn(Optional.of(character));
        when(command.getSheetStateJson()).thenReturn(null);
        when(command.getInventoryJson()).thenReturn(null);
        when(campaignCharacterRepository.save(character)).thenReturn(character);

        // When
        var result = usecase.execute(campaignId, command);

        // Then
        assertSame(character, result);
        verify(character).updateSheet("{}", "[]");
        verify(campaignCharacterRepository).save(character);
    }

    @Test
    void execute_characterNotFound_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var command = Instancio.create(UpdateCampaignCharacterRequest.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignCharacterRepository.findByCampaignIdAndUserId(campaignId, userId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId, command));

        // Then
        assertEquals("campaign character not found", exception.getMessage());
    }
}
