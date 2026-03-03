package com.craftpg.application.usecase.character.getcampaigncharacter;

import com.craftpg.domain.model.CampaignCharacter;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public interface GetCampaignCharacterUsecase {

    CampaignCharacter execute(@NonNull final UUID campaignId);
}
