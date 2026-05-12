package com.craftpg.application.mapper;

import com.craftpg.application.usecase.invite.getinvitebytoken.InvitePreviewData;
import com.craftpg.domain.model.campaign.CampaignInvite;
import com.craftpg.infrastructure.web.dto.CampaignIdResponse;
import com.craftpg.infrastructure.web.dto.InvitePreviewResponse;
import com.craftpg.infrastructure.web.dto.PendingInviteResponse;
import com.craftpg.infrastructure.web.dto.TokenResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class InviteMapper {

    public TokenResponse toTokenResponse(final String token) {
        return new TokenResponse(token);
    }

    public CampaignIdResponse toCampaignIdResponse(final UUID campaignId) {
        return new CampaignIdResponse(campaignId);
    }

    public InvitePreviewResponse toPreviewResponse(final InvitePreviewData previewData) {
        final CampaignInvite invite = previewData.invite();
        return new InvitePreviewResponse(
                invite.getCampaignId(),
                previewData.campaignTitle(),
                previewData.dmName(),
                invite.getEmail(),
                parseRoles(invite.getRolesJson()),
                invite.getExpiresAt(),
                invite.isAccepted()
        );
    }

    public PendingInviteResponse toPendingInviteResponse(final CampaignInvite invite) {
        return new PendingInviteResponse(
                invite.getId(),
                invite.getCampaignId(),
                invite.getEmail(),
                parseRoles(invite.getRolesJson()),
                invite.getCreatedAt(),
                invite.getExpiresAt()
        );
    }

    private List<String> parseRoles(final String rolesJson) {
        var raw = rolesJson.replace("[", "").replace("]", "").replace("\"", "").trim();
        if (raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split(",")).map(String::trim).filter(role -> !role.isBlank()).toList();
    }
}
