package com.craftpg.application.usecase.character.addcampaigncharacterxp;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.infrastructure.web.dto.AddCampaignCharacterXpRequest;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public interface AddCampaignCharacterXpUsecase {

    CampaignCharacter execute(@NonNull final UUID campaignId, @NonNull final UUID campaignCharacterId, @NonNull final AddCampaignCharacterXpRequest command);
}
