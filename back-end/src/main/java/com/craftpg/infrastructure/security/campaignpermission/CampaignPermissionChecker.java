package com.craftpg.infrastructure.security.campaignpermission;

import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface CampaignPermissionChecker {

    boolean canViewCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId);

    boolean canEditCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId);

    boolean canDeleteCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId);

    boolean canFinishCampaign(@NonNull final UUID campaignId, @NonNull final UUID userId);

    boolean canInvite(@NonNull final UUID campaignId, @NonNull final UUID userId);

    boolean canEditSession(@NonNull final UUID campaignId, @NonNull final UUID userId);
}
