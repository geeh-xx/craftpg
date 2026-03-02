package com.craftpg.application.usecase.character.generaterandomcharacter;

import com.craftpg.application.mapper.CharacterBaseCreateInputMapper;
import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerateRandomCharacterUsecaseImpl implements GenerateRandomCharacterUsecase {

    private final CharacterBaseRepository characterBaseRepository;
    private final CurrentUserProvider currentUserProvider;
    private final CharacterBaseCreateInputMapper characterBaseCreateInputMapper;

    @Override
    @Transactional
    public CharacterBase execute() {
        var createInput = characterBaseCreateInputMapper.toRandomCreateInput(currentUserProvider.getCurrentUserId());
        return characterBaseRepository.save(CharacterBase.create(createInput));
    }
}
