package com.craftpg.application.usecase.campaign.createcampaign;

import com.craftpg.application.mapper.CampaignCreateInputMapper;
import com.craftpg.domain.model.Campaign;
import com.craftpg.domain.model.CampaignRole;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import com.craftpg.shared.constants.CampaignRoleType;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import org.jspecify.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCampaignUsecaseImpl implements CreateCampaignUsecase {

    private final CampaignRepository campaignRepository;
    private final CampaignRoleRepository campaignRoleRepository;
    private final CurrentUserProvider currentUserProvider;
    private final CampaignCreateInputMapper campaignCreateInputMapper;

    @Override
    @Transactional
    public Campaign execute(@NonNull final CreateCampaignRequest command) {
        var userId = currentUserProvider.getCurrentUserId();
        var campaign = campaignRepository.save(Campaign.create(campaignCreateInputMapper.toCreateInput(command)));
        campaignRoleRepository.save(CampaignRole.create(campaign.getId(), userId, CampaignRoleType.DM));
        return campaign;
    }
}
