package com.craftpg.application.mapper;

import com.craftpg.domain.input.CreateCharacterBaseInput;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import org.jspecify.annotations.NonNull;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CharacterBaseCreateInputMapper {

    @NonNull
    public CreateCharacterBaseInput toCreateInput(@NonNull final UUID ownerUserId, @NonNull final CreateCharacterRequest request) {
        var attributesJson = request.getAttributesJson() == null ? "{}" : request.getAttributesJson();
        return new CreateCharacterBaseInput(
            ownerUserId,
            request.getName(),
            request.getRace(),
            request.getClazz(),
            attributesJson
        );
    }

    @NonNull
    public CreateCharacterBaseInput toRandomCreateInput(@NonNull final UUID ownerUserId) {
        return new CreateCharacterBaseInput(
            ownerUserId,
            "Herói Aleatório",
            "Humano",
            "Guerreiro",
            "{\"for\":14,\"des\":12,\"con\":13,\"int\":10,\"sab\":11,\"car\":9}"
        );
    }
}
