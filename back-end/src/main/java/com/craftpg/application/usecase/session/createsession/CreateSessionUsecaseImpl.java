package com.craftpg.application.usecase.session.createsession;

import com.craftpg.application.mapper.CampaignSessionCreateInputMapper;
import com.craftpg.domain.model.CampaignSession;
import com.craftpg.infrastructure.persistence.repository.CampaignSessionRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import com.craftpg.infrastructure.web.dto.CreateSessionRequest;
import lombok.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSessionUsecaseImpl implements CreateSessionUsecase {

    private final CampaignSessionRepository campaignSessionRepository;
    private final CampaignSessionCreateInputMapper campaignSessionCreateInputMapper;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.EDIT_SESSION)
    public CampaignSession execute(@NonNull final UUID campaignId, @NonNull final CreateSessionRequest command) {
        var createInput = campaignSessionCreateInputMapper.toCreateInput(campaignId, command);
        return campaignSessionRepository.save(CampaignSession.create(createInput));
    }
}
