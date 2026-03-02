package com.craftpg.infrastructure.security.campaignpermission;

import lombok.NonNull;

import com.craftpg.infrastructure.persistence.repository.CampaignMembershipRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampaignPermissionCheckerImpl implements CampaignPermissionChecker {

    private final CampaignRoleRepository campaignRoleRepository;
    private final CampaignMembershipRepository campaignMembershipRepository;

    private boolean hasRole(@NonNull final UUID campaignId, @NonNull final UUID userId, @NonNull final String role) {
        return campaignRoleRepository.existsByIdCampaignIdAndIdUserIdAndIdRole(campaignId, userId, role);
    }

    @Override
    public boolean canViewCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId) {
        return campaignMembershipRepository.existsByIdCampaignIdAndIdUserId(campaignId, userId)
            || hasRole(campaignId, userId, "DM")
            || hasRole(campaignId, userId, "MODERATOR")
            || hasRole(campaignId, userId, "PLAYER");
    }

    @Override
    public boolean canEditCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId) {
        return hasRole(campaignId, userId, "DM") || hasRole(campaignId, userId, "MODERATOR");
    }

    @Override
    public boolean canDeleteCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId) {
        return hasRole(campaignId, userId, "DM");
    }

    @Override
    public boolean canFinishCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId) {
        return hasRole(campaignId, userId, "DM");
    }

    @Override
    public boolean canInvite(@NonNull final UUID campaignId, @NonNull final UUID userId) {
        return canEditCampaign(campaignId, userId);
    }

    @Override
    public boolean canEditSession(@NonNull final UUID campaignId, @NonNull final UUID userId) {
        return canEditCampaign(campaignId, userId);
    }
}
