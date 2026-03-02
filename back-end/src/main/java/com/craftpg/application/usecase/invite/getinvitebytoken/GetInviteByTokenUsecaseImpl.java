package com.craftpg.application.usecase.invite.getinvitebytoken;

import lombok.NonNull;

import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.shared.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetInviteByTokenUsecaseImpl implements GetInviteByTokenUsecase {

    private final CampaignInviteRepository campaignInviteRepository;

    @Override
    @Transactional(readOnly = true)
    public CampaignInvite execute(@NonNull final String token) {
        return campaignInviteRepository.findByTokenHash(HashUtil.sha256(token)).orElseThrow(() -> new ApiException("invite not found"));
    }
}
