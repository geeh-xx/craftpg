package com.craftpg.application.mapper;

import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.application.usecase.invite.getinvitebytoken.InvitePreviewData;
import com.craftpg.infrastructure.web.dto.CampaignIdResponse;
import com.craftpg.infrastructure.web.dto.InvitePreviewResponse;
import com.craftpg.infrastructure.web.dto.PendingInviteResponse;
import com.craftpg.infrastructure.web.dto.TokenResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class InviteMapper {

    @NonNull
    public TokenResponse toTokenResponse(@NonNull final String token) {
        return new TokenResponse(token);
    }

    @NonNull
    public CampaignIdResponse toCampaignIdResponse(@NonNull final UUID campaignId) {
        return new CampaignIdResponse(campaignId);
    }

    @NonNull
    public InvitePreviewResponse toPreviewResponse(@NonNull final InvitePreviewData previewData) {
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

    @NonNull
    public PendingInviteResponse toPendingInviteResponse(@NonNull final CampaignInvite invite) {
        return new PendingInviteResponse(
            invite.getId(),
            invite.getCampaignId(),
            invite.getEmail(),
            parseRoles(invite.getRolesJson()),
            invite.getCreatedAt(),
            invite.getExpiresAt()
        );
    }

    @NonNull
    private List<String> parseRoles(@NonNull final String rolesJson) {
        var raw = rolesJson.replace("[", "").replace("]", "").replace("\"", "").trim();
        if (raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split(",")).map(String::trim).filter(role -> !role.isBlank()).toList();
    }
}
