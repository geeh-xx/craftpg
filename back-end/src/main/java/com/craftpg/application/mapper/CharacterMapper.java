package com.craftpg.application.mapper;

import com.craftpg.domain.input.CreateCharacterBaseInput;
import com.craftpg.domain.model.Character.CharacterBase;
import com.craftpg.domain.model.campaign.CampaignCharacter;
import com.craftpg.infrastructure.web.dto.CampaignCharacterResponse;
import com.craftpg.infrastructure.web.dto.CharacterResponse;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CharacterMapper {

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

    public CreateCharacterBaseInput toRandomCreateInput(@NonNull final UUID ownerUserId) {
        return new CreateCharacterBaseInput(
                ownerUserId,
                "Herói Aleatório",
                "Humano",
                "Guerreiro",
                "{\"for\":14,\"des\":12,\"con\":13,\"int\":10,\"sab\":11,\"car\":9}"
        );
    }

    public CharacterResponse toResponse(@NonNull final CharacterBase character) {
        return new CharacterResponse(character.getId(), character.getName())
                .race(character.getRace())
                .clazz(character.getClazz())
                .attributesJson(character.getAttributesJson());
    }

    public CampaignCharacterResponse toCampaignCharacterResponse(@NonNull final CampaignCharacter character) {
        return new CampaignCharacterResponse(
                character.getId(),
                character.getCampaignId(),
                character.getCharacterBaseId(),
                character.getUserId(),
                character.getLevel(),
                character.getXp(),
                character.getLocked()
        )
                .sheetStateJson(character.getSheetStateJson())
                .inventoryJson(character.getInventoryJson());
    }
}
