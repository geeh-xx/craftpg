package com.craftpg.application.usecase.character.addcampaigncharacterxp;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignCharacterRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import com.craftpg.infrastructure.web.dto.AddCampaignCharacterXpRequest;
import lombok.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddCampaignCharacterXpUsecaseImpl implements AddCampaignCharacterXpUsecase {

    private final CampaignCharacterRepository campaignCharacterRepository;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.EDIT_SESSION)
    public CampaignCharacter execute(@NonNull final UUID campaignId, @NonNull final UUID campaignCharacterId, @NonNull final AddCampaignCharacterXpRequest command) {
        var character = campaignCharacterRepository.findByCampaignIdAndId(campaignId, campaignCharacterId)
            .orElseThrow(() -> new ApiException("campaign character not found"));

        character.addXpAndApplyHappyPathLevelUp(command.getGainedXp());
        return campaignCharacterRepository.save(character);
    }
}
