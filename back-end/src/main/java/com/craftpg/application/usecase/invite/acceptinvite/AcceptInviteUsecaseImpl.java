package com.craftpg.application.usecase.invite.acceptinvite;

import lombok.NonNull;

import com.craftpg.domain.model.CampaignCharacter;
import com.craftpg.domain.model.CampaignMembership;
import com.craftpg.domain.model.CampaignRole;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignCharacterRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignMembershipRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.persistence.repository.CharacterBaseRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.shared.constants.CampaignRoleType;
import com.craftpg.shared.util.HashUtil;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AcceptInviteUsecaseImpl implements AcceptInviteUsecase {

    private final CampaignInviteRepository campaignInviteRepository;
    private final CampaignRoleRepository campaignRoleRepository;
    private final CampaignMembershipRepository campaignMembershipRepository;
    private final CharacterBaseRepository characterBaseRepository;
    private final CampaignCharacterRepository campaignCharacterRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public UUID execute(@NonNull final String token, @NonNull final UUID characterBaseId) {
        var userId = currentUserProvider.getCurrentUserId();
        var invite = campaignInviteRepository.findByTokenHash(HashUtil.sha256(token)).orElseThrow(() -> new ApiException("invite not found"));
        if (invite.isAccepted()) {
            throw new ApiException("invite already accepted");
        }
        if (invite.isExpired()) {
            throw new ApiException("invite expired");
        }
        if (campaignMembershipRepository.existsByIdCampaignIdAndIdUserId(invite.getCampaignId(), userId)) {
            throw new ApiException("user already in campaign");
        }
        var characterBase = characterBaseRepository.findById(characterBaseId).orElseThrow(() -> new ApiException("character not found"));
        if (!characterBase.getOwnerUserId().equals(userId)) {
            throw new ApiException("invalid character ownership");
        }

        var campaignCharacter = campaignCharacterRepository.save(CampaignCharacter.fromBase(invite.getCampaignId(), userId, characterBaseId));
        campaignMembershipRepository.save(CampaignMembership.create(invite.getCampaignId(), userId, campaignCharacter.getId()));

        var roles = invite.getRolesJson().replace("[", "").replace("]", "").replace("\"", "");
        if (!roles.isBlank()) {
            Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(r -> !r.isBlank() && !"DM".equals(r))
                .forEach(role -> campaignRoleRepository.save(CampaignRole.create(invite.getCampaignId(), userId, CampaignRoleType.valueOf(role))));
        }

        invite.accept(userId);
        campaignInviteRepository.save(invite);
        return invite.getCampaignId();
    }
}
