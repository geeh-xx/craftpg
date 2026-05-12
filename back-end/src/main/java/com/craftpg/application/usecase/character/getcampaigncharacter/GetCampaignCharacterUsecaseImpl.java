package com.craftpg.application.usecase.character.getcampaigncharacter;

import com.craftpg.domain.model.campaign.CampaignCharacter;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignCharacterRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCampaignCharacterUsecaseImpl implements GetCampaignCharacterUsecase {

    private final CampaignCharacterRepository campaignCharacterRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional(readOnly = true)
    public CampaignCharacter execute(@NonNull final UUID campaignId) {
        return campaignCharacterRepository
                .findByCampaignIdAndUserId(campaignId, currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ApiException("campaign character not found"));
    }
}
