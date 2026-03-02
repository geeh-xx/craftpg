package com.craftpg.application.usecase.campaign.deletecampaign;

import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import lombok.NonNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCampaignUsecaseImpl implements DeleteCampaignUsecase {

    private final CampaignRepository campaignRepository;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.DELETE)
    public void execute(@NonNull final UUID campaignId) {
        campaignRepository.deleteById(campaignId);
    }
}
