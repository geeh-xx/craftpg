package com.craftpg.application.usecase.character;

import com.craftpg.application.mapper.CharacterMapper;
import com.craftpg.domain.input.CreateCharacterBaseInput;
import com.craftpg.domain.model.Character.CharacterBase;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.CharacterResponse;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import com.craftpg.infrastructure.web.dto.UpdateCharacterRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterManagementUseCaseImplTest {

    @Mock
    private CharacterBaseRepository characterBaseRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private CharacterMapper characterMapper;

    @InjectMocks
    private CharacterManagementUseCaseImpl useCase;

    @Test
    void ShouldReturnCharacterResponse_WhenCreateWithValidRequest() {
        var userId = UUID.randomUUID();
        var request = Instancio.create(CreateCharacterRequest.class);
        var createInput = new CreateCharacterBaseInput(userId, request.getName(), request.getRace(), request.getClazz(), request.getAttributesJson());
        var character = CharacterBase.create(createInput);
        var response = Instancio.create(CharacterResponse.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterMapper.toCreateInput(userId, request)).thenReturn(createInput);
        when(characterBaseRepository.save(any(CharacterBase.class))).thenReturn(character);
        when(characterMapper.toResponse(character)).thenReturn(response);

        var result = useCase.create(request);

        assertThat(result.isNotSuccess()).isFalse();
        assertThat(result.getValue()).contains(response);
    }

    @Test
    void ShouldReturnPaginatedResponse_WhenFindAllWithPageable() {
        var userId = UUID.randomUUID();
        var pageable = PageRequest.of(0, 10);
        var character = CharacterBase.create(new CreateCharacterBaseInput(userId, "Hero", "Human", "Warrior", "{}"));
        var response = Instancio.create(CharacterResponse.class);
        var page = new PageImpl<>(java.util.List.of(character), pageable, 1);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterBaseRepository.findPageByOwnerUserId(userId, pageable)).thenReturn(page);
        when(characterMapper.toResponse(character)).thenReturn(response);

        var result = useCase.findAll(pageable);

        assertThat(result.isNotSuccess()).isFalse();
        assertThat(result.getValue()).isPresent();
        assertThat(result.getValue().get().getContent()).containsExactly(response);
    }

    @Test
    void ShouldReturnFailure_WhenFindByIdWithMissingCharacter() {
        var userId = UUID.randomUUID();
        var characterId = UUID.randomUUID();

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterBaseRepository.findByIdAndOwnerUserId(characterId, userId)).thenReturn(Optional.empty());

        var result = useCase.findById(characterId);

        assertThat(result.isNotSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("character not found");
    }

    @Test
    void ShouldReturnCharacterResponse_WhenUpdateWithOwnedCharacter() {
        var userId = UUID.randomUUID();
        var characterId = UUID.randomUUID();
        var character = CharacterBase.create(new CreateCharacterBaseInput(userId, "Old", "Human", "Warrior", "{}"));
        var updateInput = new UpdateCharacterRequest();
        updateInput.setCharacterId(characterId);
        updateInput.setName("New Name");
        updateInput.setRace("Elf");
        updateInput.setClazz("Mage");
        updateInput.setAttributesJson("{\"int\":16}");
        var response = Instancio.create(CharacterResponse.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterBaseRepository.findByIdAndOwnerUserId(characterId, userId)).thenReturn(Optional.of(character));
        when(characterBaseRepository.save(character)).thenReturn(character);
        when(characterMapper.toResponse(character)).thenReturn(response);

        var result = useCase.update(updateInput);

        assertThat(result.isNotSuccess()).isFalse();
        assertThat(result.getValue()).contains(response);
        assertThat(character.getName()).isEqualTo("New Name");
    }

    @Test
    void ShouldReturnFailure_WhenDeleteWithMissingCharacter() {
        var userId = UUID.randomUUID();
        var characterId = UUID.randomUUID();

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterBaseRepository.existsByIdAndOwnerUserId(characterId, userId)).thenReturn(false);

        var result = useCase.delete(characterId);

        assertThat(result.isNotSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("character not found");
    }

    @Test
    void ShouldReturnSuccess_WhenDeleteWithOwnedCharacter() {
        var userId = UUID.randomUUID();
        var characterId = UUID.randomUUID();

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(characterBaseRepository.existsByIdAndOwnerUserId(characterId, userId)).thenReturn(true);

        var result = useCase.delete(characterId);

        assertThat(result.isNotSuccess()).isFalse();
        verify(characterBaseRepository).deleteById(characterId);
    }
}
