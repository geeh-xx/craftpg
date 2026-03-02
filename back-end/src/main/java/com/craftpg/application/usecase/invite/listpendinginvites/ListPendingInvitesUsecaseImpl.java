package com.craftpg.application.usecase.invite.listpendinginvites;

import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListPendingInvitesUsecaseImpl implements ListPendingInvitesUsecase {

    private final CampaignInviteRepository campaignInviteRepository;

    @Override
    @RequireCampaignPermission(action = CampaignPermissionAction.INVITE)
    public List<CampaignInvite> execute(@NonNull UUID campaignId) {
        return campaignInviteRepository.findAllByCampaignIdAndAcceptedAtIsNullOrderByCreatedAtDesc(campaignId);
    }
}
