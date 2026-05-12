package com.craftpg.application.usecase.character.updatecampaigncharacter;

import com.craftpg.domain.model.campaign.CampaignCharacter;
import com.craftpg.infrastructure.web.dto.UpdateCampaignCharacterRequest;

import java.util.UUID;

public interface UpdateCampaignCharacterUsecase {

    CampaignCharacter execute(final UUID campaignId, final UpdateCampaignCharacterRequest command);
}
