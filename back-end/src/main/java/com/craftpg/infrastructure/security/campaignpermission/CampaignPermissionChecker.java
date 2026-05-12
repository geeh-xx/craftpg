package com.craftpg.infrastructure.security.campaignpermission;

import java.util.UUID;

public interface CampaignPermissionChecker {

    boolean canViewCampaign(final UUID campaignId, final UUID userId);

    boolean canEditCampaign(final UUID campaignId, final UUID userId);

    boolean canDeleteCampaign(final UUID campaignId, final UUID userId);

    boolean canFinishCampaign(final UUID campaignId, final UUID userId);

    boolean canInvite(final UUID campaignId, final UUID userId);

    boolean canEditSession(final UUID campaignId, final UUID userId);
}
