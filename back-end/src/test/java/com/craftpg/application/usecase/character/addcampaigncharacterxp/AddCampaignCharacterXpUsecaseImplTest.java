package com.craftpg.application.usecase.character.addcampaigncharacterxp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignCharacterRepository;
import com.craftpg.infrastructure.web.dto.AddCampaignCharacterXpRequest;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddCampaignCharacterXpUsecaseImplTest {

    @Mock
    private CampaignCharacterRepository campaignCharacterRepository;

    @InjectMocks
    private AddCampaignCharacterXpUsecaseImpl usecase;

    @Test
    void execute_existingCharacter_addsXpAndSaves() {
        // Given
        var campaignId = UUID.randomUUID();
        var campaignCharacterId = UUID.randomUUID();
        var command = mock(AddCampaignCharacterXpRequest.class);
        var character = mock(CampaignCharacter.class);

        when(command.getGainedXp()).thenReturn(350);
        when(campaignCharacterRepository.findByCampaignIdAndId(campaignId, campaignCharacterId)).thenReturn(Optional.of(character));
        when(campaignCharacterRepository.save(character)).thenReturn(character);

        // When
        var result = usecase.execute(campaignId, campaignCharacterId, command);

        // Then
        assertSame(character, result);
        verify(character).addXpAndApplyHappyPathLevelUp(350);
        verify(campaignCharacterRepository).save(character);
    }

    @Test
    void execute_characterNotFound_throwsApiException() {
        // Given
        var campaignId = UUID.randomUUID();
        var campaignCharacterId = UUID.randomUUID();
        var command = mock(AddCampaignCharacterXpRequest.class);

        when(campaignCharacterRepository.findByCampaignIdAndId(campaignId, campaignCharacterId)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(campaignId, campaignCharacterId, command));

        // Then
        assertEquals("campaign character not found", exception.getMessage());
    }
}
