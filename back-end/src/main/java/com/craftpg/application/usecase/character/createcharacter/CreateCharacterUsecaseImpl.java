package com.craftpg.application.usecase.character.createcharacter;

import com.craftpg.application.mapper.CharacterBaseCreateInputMapper;
import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCharacterUsecaseImpl implements CreateCharacterUsecase {

    private final CharacterBaseRepository characterBaseRepository;
    private final CurrentUserProvider currentUserProvider;
    private final CharacterBaseCreateInputMapper characterBaseCreateInputMapper;

    @Override
    @Transactional
    public CharacterBase execute(@NonNull final CreateCharacterRequest command) {
        var createInput = characterBaseCreateInputMapper.toCreateInput(currentUserProvider.getCurrentUserId(), command);
        var character = CharacterBase.create(createInput);
        return characterBaseRepository.save(character);
    }
}
