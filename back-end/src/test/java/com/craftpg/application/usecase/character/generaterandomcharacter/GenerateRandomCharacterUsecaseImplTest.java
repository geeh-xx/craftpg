package com.craftpg.application.usecase.character.generaterandomcharacter;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.application.mapper.CharacterBaseCreateInputMapper;
import com.craftpg.domain.input.CreateCharacterBaseInput;
import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenerateRandomCharacterUsecaseImplTest {

    @Mock
    private CharacterBaseRepository characterBaseRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private CharacterBaseCreateInputMapper characterBaseCreateInputMapper;

    @InjectMocks
    private GenerateRandomCharacterUsecaseImpl usecase;

    @Test
    void execute_currentUser_generatesAndPersistsCharacter() {
        // Given
        var userId = UUID.randomUUID();
        var input = Instancio.create(CreateCharacterBaseInput.class);
        var savedCharacter = mock(CharacterBase.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterBaseCreateInputMapper.toRandomCreateInput(userId)).thenReturn(input);
        when(characterBaseRepository.save(any(CharacterBase.class))).thenReturn(savedCharacter);

        // When
        var result = usecase.execute();

        // Then
        assertSame(savedCharacter, result);
        verify(characterBaseCreateInputMapper).toRandomCreateInput(userId);
        verify(characterBaseRepository).save(any(CharacterBase.class));
    }
}
