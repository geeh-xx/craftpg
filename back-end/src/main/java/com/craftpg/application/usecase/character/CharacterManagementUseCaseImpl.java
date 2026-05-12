package com.craftpg.application.usecase.character;

import com.craftpg.application.mapper.CharacterMapper;
import com.craftpg.application.usecase.OperationResult;
import com.craftpg.domain.model.Character.CharacterBase;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.CharacterResponse;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import com.craftpg.infrastructure.web.dto.UpdateCharacterRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CharacterManagementUseCaseImpl implements CharacterManagementUseCase {

    private final CharacterBaseRepository characterBaseRepository;
    private final CurrentUserProvider currentUserProvider;
    private final CharacterMapper characterMapper;

    @Override
    @Transactional
    public OperationResult<CharacterResponse> create(@NonNull final CreateCharacterRequest input) {
        var userId = currentUserProvider.getCurrentUserId();
        var createInput = characterMapper.toCreateInput(userId, input);
        var character = characterBaseRepository.save(CharacterBase.create(createInput));
        return OperationResult.ok(characterMapper.toResponse(character));
    }

    @Override
    @Transactional
    public OperationResult<CharacterResponse> update(@NonNull final UpdateCharacterRequest input) {
        var userId = currentUserProvider.getCurrentUserId();
        var characterOptional = characterBaseRepository.findByIdAndOwnerUserId(input.getCharacterId(), userId);
        if (characterOptional.isEmpty()) {
            return OperationResult.failure("character not found");
        }

        var attributesJson = input.getAttributesJson() == null ? "{}" : input.getAttributesJson();
        var character = characterOptional.get();
        character.update(input.getName(), input.getRace(), input.getClazz(), attributesJson);
        var updatedCharacter = characterBaseRepository.save(character);
        return OperationResult.ok(characterMapper.toResponse(updatedCharacter));
    }

    @Override
    @Transactional(readOnly = true)
    public OperationResult<CharacterResponse> findById(@NonNull final UUID id) {
        var userId = currentUserProvider.getCurrentUserId();
        var characterOptional = characterBaseRepository.findByIdAndOwnerUserId(id, userId);
        if (characterOptional.isEmpty()) {
            return OperationResult.failure("character not found");
        }
        return OperationResult.ok(characterMapper.toResponse(characterOptional.get()));
    }

    @Transactional(readOnly = true)
    public OperationResult<Page<CharacterResponse>> findAll(@NonNull final Pageable pageable) {
        var userId = currentUserProvider.getCurrentUserId();
        var characters = characterBaseRepository.findPageByOwnerUserId(userId, pageable)
                .map(characterMapper::toResponse);
        return OperationResult.ok(characters);
    }

    @Override
    @Transactional
    public OperationResult<Void> delete(@NonNull final UUID id) {
        var userId = currentUserProvider.getCurrentUserId();
        if (!characterBaseRepository.existsByIdAndOwnerUserId(id, userId)) {
            return OperationResult.failure("character not found");
        }
        characterBaseRepository.deleteById(id);
        return OperationResult.ok();
    }
}
