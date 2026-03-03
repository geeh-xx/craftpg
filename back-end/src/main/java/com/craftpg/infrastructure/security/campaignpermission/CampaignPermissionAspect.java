package com.craftpg.infrastructure.security.campaignpermission;

import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import org.jspecify.annotations.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CampaignPermissionAspect {

    private final CampaignPermissionChecker campaignPermissionChecker;
    private final CurrentUserProvider currentUserProvider;

    @Before("@annotation(requireCampaignPermission)")
    public void validatePermission(@NonNull final JoinPoint joinPoint, @NonNull final RequireCampaignPermission requireCampaignPermission) {
        var args = joinPoint.getArgs();
        var campaignIdArgIndex = requireCampaignPermission.campaignIdArgIndex();
        if (campaignIdArgIndex < 0 || campaignIdArgIndex >= args.length) {
            throw new IllegalStateException("invalid campaignIdArgIndex in @RequireCampaignPermission");
        }
        if (!(args[campaignIdArgIndex] instanceof UUID campaignId)) {
            throw new IllegalStateException("@RequireCampaignPermission expects UUID campaignId argument");
        }

        var userId = currentUserProvider.getCurrentUserId();
        var allowed = switch (requireCampaignPermission.action()) {
            case VIEW -> campaignPermissionChecker.canViewCampaign(campaignId, userId);
            case EDIT -> campaignPermissionChecker.canEditCampaign(campaignId, userId);
            case DELETE -> campaignPermissionChecker.canDeleteCampaign(campaignId, userId);
            case FINISH -> campaignPermissionChecker.canFinishCampaign(campaignId, userId);
            case INVITE -> campaignPermissionChecker.canInvite(campaignId, userId);
            case EDIT_SESSION -> campaignPermissionChecker.canEditSession(campaignId, userId);
        };

        if (!allowed) {
            throw new ApiException("forbidden");
        }
    }
}
