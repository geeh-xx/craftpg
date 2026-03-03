package com.craftpg.application.usecase.character.createcharacter;

import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import org.jspecify.annotations.NonNull;

public interface CreateCharacterUsecase {

    CharacterBase execute(@NonNull final CreateCharacterRequest command);
}
