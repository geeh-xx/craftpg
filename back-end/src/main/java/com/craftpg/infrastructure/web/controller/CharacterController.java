package com.craftpg.infrastructure.web.controller;

import org.jspecify.annotations.NonNull;

import com.craftpg.application.mapper.CharacterMapper;
import com.craftpg.application.usecase.character.createcharacter.CreateCharacterUsecase;
import com.craftpg.application.usecase.character.addcampaigncharacterxp.AddCampaignCharacterXpUsecase;
import com.craftpg.application.usecase.character.generaterandomcharacter.GenerateRandomCharacterUsecase;
import com.craftpg.application.usecase.character.getcampaigncharacter.GetCampaignCharacterUsecase;
import com.craftpg.application.usecase.character.listcharacter.ListCharacterUsecase;
import com.craftpg.application.usecase.character.updatecampaigncharacter.UpdateCampaignCharacterUsecase;
import com.craftpg.infrastructure.web.api.CharactersApi;
import com.craftpg.infrastructure.web.dto.AddCampaignCharacterXpRequest;
import com.craftpg.infrastructure.web.dto.CampaignCharacterResponse;
import com.craftpg.infrastructure.web.dto.CharacterResponse;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import com.craftpg.infrastructure.web.dto.UpdateCampaignCharacterRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CharacterController implements CharactersApi {

    private final ListCharacterUsecase listCharacterUsecase;
    private final CreateCharacterUsecase createCharacterUsecase;
    private final GenerateRandomCharacterUsecase generateRandomCharacterUsecase;
    private final GetCampaignCharacterUsecase getCampaignCharacterUsecase;
    private final UpdateCampaignCharacterUsecase updateCampaignCharacterUsecase;
    private final AddCampaignCharacterXpUsecase addCampaignCharacterXpUsecase;
    private final CharacterMapper characterMapper;

    @Override
    public ResponseEntity<List<CharacterResponse>> charactersGet() {
        return ResponseEntity.ok(listCharacterUsecase.execute().stream().map(characterMapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<CharacterResponse> charactersPost(@NonNull final CreateCharacterRequest createCharacterRequest) {
        return ResponseEntity.status(201).body(characterMapper.toResponse(createCharacterUsecase.execute(createCharacterRequest)));
    }

    @Override
    public ResponseEntity<CharacterResponse> charactersGenerateRandomPost() {
        return ResponseEntity.status(201).body(characterMapper.toResponse(generateRandomCharacterUsecase.execute()));
    }

    @Override
    public ResponseEntity<CampaignCharacterResponse> campaignsCampaignIdCharactersMeGet(@NonNull final UUID campaignId) {
        return ResponseEntity.ok(characterMapper.toCampaignCharacterResponse(getCampaignCharacterUsecase.execute(campaignId)));
    }

    @Override
    public ResponseEntity<CampaignCharacterResponse> campaignsCampaignIdCharactersMePatch(
        @NonNull final UUID campaignId,
        @NonNull final UpdateCampaignCharacterRequest updateCampaignCharacterRequest
    ) {
        return ResponseEntity.ok(characterMapper.toCampaignCharacterResponse(updateCampaignCharacterUsecase.execute(campaignId, updateCampaignCharacterRequest)));
    }

    @Override
    public ResponseEntity<CampaignCharacterResponse> campaignsCampaignIdCharactersCampaignCharacterIdXpPost(
        @NonNull final UUID campaignId,
        @NonNull final UUID campaignCharacterId,
        @NonNull final AddCampaignCharacterXpRequest addCampaignCharacterXpRequest
    ) {
        return ResponseEntity.ok(characterMapper.toCampaignCharacterResponse(addCampaignCharacterXpUsecase.execute(campaignId, campaignCharacterId, addCampaignCharacterXpRequest)));
    }
}
