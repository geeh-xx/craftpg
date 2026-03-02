package com.craftpg.application.usecase.campaign.listcampaign;

import com.craftpg.domain.model.Campaign;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListCampaignUsecaseImpl implements ListCampaignUsecase {

    private final CampaignRepository campaignRepository;
    private final CampaignRoleRepository campaignRoleRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional(readOnly = true)
    public List<Campaign> execute() {
        var userId = currentUserProvider.getCurrentUserId();
        var ids = campaignRoleRepository.findByIdUserId(userId).stream().map(role -> role.getId().getCampaignId()).distinct().toList();
        return campaignRepository.findAllById(ids);
    }
}
