package com.craftpg.application.usecase.character.getcampaigncharacter;

import com.craftpg.domain.model.campaign.CampaignCharacter;

import java.util.UUID;

public interface GetCampaignCharacterUsecase {

    CampaignCharacter execute(final UUID campaignId);
}
