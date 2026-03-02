package com.craftpg.application.usecase.character.listcharacter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListCharacterUsecaseImplTest {

    @Mock
    private CharacterBaseRepository characterBaseRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ListCharacterUsecaseImpl usecase;

    @Test
    void execute_currentUser_returnsOwnedCharacters() {
        // Given
        var userId = UUID.randomUUID();
        var characters = List.of(mock(CharacterBase.class), mock(CharacterBase.class));

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterBaseRepository.findByOwnerUserId(userId)).thenReturn(characters);

        // When
        var result = usecase.execute();

        // Then
        assertEquals(2, result.size());
        verify(characterBaseRepository).findByOwnerUserId(userId);
    }
}
