package com.craftpg.application.usecase.invite.getinvitebytoken;

import lombok.NonNull;

import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.shared.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetInviteByTokenUsecaseImpl implements GetInviteByTokenUsecase {

    private final CampaignInviteRepository campaignInviteRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignRoleRepository campaignRoleRepository;
    private final AppUserRepository appUserRepository;

    @Override
    @Transactional(readOnly = true)
    public InvitePreviewData execute(@NonNull final String token) {
        final var invite = campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))
            .orElseThrow(() -> new ApiException("invite not found"));

        final var campaign = campaignRepository.findById(invite.getCampaignId())
            .orElseThrow(() -> new ApiException("campaign not found"));

        final var dmName = campaignRoleRepository
            .findFirstByIdCampaignIdAndIdRole(invite.getCampaignId(), "DM")
            .flatMap(campaignRole -> appUserRepository.findById(campaignRole.getId().getUserId()))
            .map(appUser -> appUser.getDisplayName() != null && !appUser.getDisplayName().isBlank()
                ? appUser.getDisplayName()
                : appUser.getEmail())
            .orElse("Unknown DM");

        return new InvitePreviewData(invite, campaign.getTitle(), dmName);
    }
}
