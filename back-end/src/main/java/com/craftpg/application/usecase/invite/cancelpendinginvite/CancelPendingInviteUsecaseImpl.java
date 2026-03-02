package com.craftpg.application.usecase.invite.cancelpendinginvite;

import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelPendingInviteUsecaseImpl implements CancelPendingInviteUsecase {

    private final CampaignInviteRepository campaignInviteRepository;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.INVITE)
    public void execute(@NonNull UUID campaignId, @NonNull UUID inviteId) {
        var invite = campaignInviteRepository.findByIdAndCampaignId(inviteId, campaignId)
            .orElseThrow(() -> new ApiException("invite not found"));

        if (invite.isAccepted()) {
            throw new ApiException("invite already accepted");
        }

        campaignInviteRepository.delete(invite);
    }
}
