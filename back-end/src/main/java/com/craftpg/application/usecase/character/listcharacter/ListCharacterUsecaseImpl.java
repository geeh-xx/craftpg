package com.craftpg.application.usecase.character.listcharacter;

import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListCharacterUsecaseImpl implements ListCharacterUsecase {

    private final CharacterBaseRepository characterBaseRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional(readOnly = true)
    public List<CharacterBase> execute() {
        return characterBaseRepository.findByOwnerUserId(currentUserProvider.getCurrentUserId());
    }
}
