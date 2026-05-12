package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.mapper.CharacterMapper;
import com.craftpg.application.usecase.character.CharacterManagementUseCase;
import com.craftpg.application.usecase.character.addcampaigncharacterxp.AddCampaignCharacterXpUsecase;
import com.craftpg.application.usecase.character.generaterandomcharacter.GenerateRandomCharacterUsecase;
import com.craftpg.application.usecase.character.getcampaigncharacter.GetCampaignCharacterUsecase;
import com.craftpg.application.usecase.character.updatecampaigncharacter.UpdateCampaignCharacterUsecase;
import com.craftpg.infrastructure.factory.UseCaseProvider;
import com.craftpg.infrastructure.web.api.CharactersApi;
import com.craftpg.infrastructure.web.dto.AddCampaignCharacterXpRequest;
import com.craftpg.infrastructure.web.dto.CampaignCharacterResponse;
import com.craftpg.infrastructure.web.dto.CharacterResponse;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import com.craftpg.infrastructure.web.dto.UpdateCampaignCharacterRequest;
import com.craftpg.infrastructure.web.dto.UpdateCharacterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CharacterController implements CharactersApi {

    private final UseCaseProvider useCaseProvider;
    private final CharacterMapper characterMapper;

    @Override
    public ResponseEntity<List<CharacterResponse>> getAllCharacters() {
        var result = useCaseProvider.getUseCase(CharacterManagementUseCase.class).findAll(Pageable.unpaged());
        if (result.isNotSuccess()) {
            return ResponseEntity.badRequest().build();
        }

        return result.getValue()
                .map(page -> ResponseEntity.ok(page.getContent()))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<CharacterResponse> createCharacter(final CreateCharacterRequest createCharacterRequest) {
        var result = useCaseProvider.getUseCase(CharacterManagementUseCase.class).create(createCharacterRequest);
        if (result.isNotSuccess()) {
            return ResponseEntity.badRequest().build();
        }

        return result.getValue()
                .map(response -> ResponseEntity.status(201).body(response))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<CharacterResponse> getCharacterById(final UUID characterId) {
        var result = useCaseProvider.getUseCase(CharacterManagementUseCase.class).findById(characterId);
        if (result.isNotSuccess()) {
            return ResponseEntity.badRequest().build();
        }

        return result.getValue()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<CharacterResponse> updateCharacterById(final UUID characterId, final UpdateCharacterRequest updateCharacterRequest
    ) {
        updateCharacterRequest.setCharacterId(characterId);
        var result = useCaseProvider.getUseCase(CharacterManagementUseCase.class).update(updateCharacterRequest);
        if (result.isNotSuccess()) {
            return ResponseEntity.badRequest().build();
        }

        return result.getValue()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> deleteCharacterById(final UUID characterId) {
        var result = useCaseProvider.getUseCase(CharacterManagementUseCase.class).delete(characterId);
        if (result.isNotSuccess()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CharacterResponse> generateRandomCharacter() {
        return ResponseEntity.status(201).body(characterMapper.toResponse(useCaseProvider.getUseCase(GenerateRandomCharacterUsecase.class).execute()));
    }

    @Override
    public ResponseEntity<CampaignCharacterResponse> getMyCampaignCharacterByCampaignId(final UUID campaignId) {
        return ResponseEntity.ok(characterMapper.toCampaignCharacterResponse(useCaseProvider.getUseCase(GetCampaignCharacterUsecase.class).execute(campaignId)));
    }

    @Override
    public ResponseEntity<CampaignCharacterResponse> updateMyCampaignCharacterByCampaignId(final UUID campaignId, final UpdateCampaignCharacterRequest updateCampaignCharacterRequest
    ) {
        return ResponseEntity.ok(characterMapper.toCampaignCharacterResponse(useCaseProvider.getUseCase(UpdateCampaignCharacterUsecase.class).execute(campaignId, updateCampaignCharacterRequest)));
    }

    @Override
    public ResponseEntity<CampaignCharacterResponse> addXpToCampaignCharacter(final UUID campaignId, final UUID campaignCharacterId, final AddCampaignCharacterXpRequest addCampaignCharacterXpRequest
    ) {
        return ResponseEntity.ok(characterMapper.toCampaignCharacterResponse(useCaseProvider.getUseCase(AddCampaignCharacterXpUsecase.class).execute(campaignId, campaignCharacterId, addCampaignCharacterXpRequest)));
    }
}
