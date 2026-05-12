package com.craftpg.application.usecase.character.addcampaigncharacterxp;

import com.craftpg.domain.model.campaign.CampaignCharacter;
import com.craftpg.infrastructure.web.dto.AddCampaignCharacterXpRequest;

import java.util.UUID;

public interface AddCampaignCharacterXpUsecase {

    CampaignCharacter execute(final UUID campaignId, final UUID campaignCharacterId, final AddCampaignCharacterXpRequest command);
}
