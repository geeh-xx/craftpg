package com.craftpg.application.usecase.character.updatecampaigncharacter;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.infrastructure.web.dto.UpdateCampaignCharacterRequest;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public interface UpdateCampaignCharacterUsecase {

    CampaignCharacter execute(@NonNull final UUID campaignId, @NonNull final UpdateCampaignCharacterRequest command);
}
