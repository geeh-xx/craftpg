package com.craftpg.domain.model;

import lombok.NonNull;

import com.craftpg.shared.constants.CampaignRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "campaign_invite")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignInvite {

    @Id
    private UUID id;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(nullable = false)
    private String email;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "roles_json", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String rolesJson;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "accepted_by_user_id")
    private UUID acceptedByUserId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static CampaignInvite create(@NonNull final UUID campaignId, @NonNull final String email, @NonNull final String tokenHash, @NonNull final Set<CampaignRoleType> roles, @NonNull final LocalDateTime expiresAt) {
        var invite = new CampaignInvite();
        invite.id = UUID.randomUUID();
        invite.campaignId = campaignId;
        invite.email = email;
        invite.tokenHash = tokenHash;
        invite.rolesJson = roles.stream().map(Enum::name).sorted().map(v -> "\"" + v + "\"").reduce("[", (a, b) -> a.equals("[") ? a + b : a + "," + b) + "]";
        invite.expiresAt = expiresAt;
        invite.createdAt = LocalDateTime.now();
        return invite;
    }

    public void accept(@NonNull final UUID userId) {
        this.acceptedByUserId = userId;
        this.acceptedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isAccepted() {
        return this.acceptedAt != null;
    }
}
