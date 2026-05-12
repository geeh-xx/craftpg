package com.craftpg.application.usecase.session.listsession;

import com.craftpg.domain.model.campaign.CampaignSession;
import com.craftpg.infrastructure.persistence.repository.CampaignSessionRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListSessionUsecaseImpl implements ListSessionUsecase {

    private final CampaignSessionRepository campaignSessionRepository;

    @Override
    @Transactional(readOnly = true)
    @RequireCampaignPermission(action = CampaignPermissionAction.VIEW)
    public List<CampaignSession> execute(@NonNull final UUID campaignId) {
        return campaignSessionRepository.findAllByCampaignIdOrderByScheduledAtAsc(campaignId);
    }
}
