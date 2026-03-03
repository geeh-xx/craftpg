package com.craftpg.application.usecase.character.updatecampaigncharacter;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignCharacterRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.UpdateCampaignCharacterRequest;
import org.jspecify.annotations.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCampaignCharacterUsecaseImpl implements UpdateCampaignCharacterUsecase {

    private final CampaignCharacterRepository campaignCharacterRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public CampaignCharacter execute(@NonNull final UUID campaignId, @NonNull final UpdateCampaignCharacterRequest command) {
        var character = campaignCharacterRepository
            .findByCampaignIdAndUserId(campaignId, currentUserProvider.getCurrentUserId())
            .orElseThrow(() -> new ApiException("campaign character not found"));

        character.updateSheet(
            command.getSheetStateJson() == null ? "{}" : command.getSheetStateJson(),
            command.getInventoryJson() == null ? "[]" : command.getInventoryJson()
        );
        return campaignCharacterRepository.save(character);
    }
}
