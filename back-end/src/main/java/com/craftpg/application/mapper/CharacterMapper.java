package com.craftpg.application.mapper;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.domain.model.CharacterBase;
import com.craftpg.infrastructure.web.dto.CampaignCharacterResponse;
import com.craftpg.infrastructure.web.dto.CharacterResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

    @NonNull
    public CharacterResponse toResponse(@NonNull final CharacterBase character) {
        return new CharacterResponse(character.getId(), character.getName())
            .race(character.getRace())
            .clazz(character.getClazz())
            .attributesJson(character.getAttributesJson());
    }

    @NonNull
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
