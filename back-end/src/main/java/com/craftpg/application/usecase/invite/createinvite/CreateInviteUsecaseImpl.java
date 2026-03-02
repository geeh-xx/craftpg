package com.craftpg.application.usecase.invite.createinvite;

import com.craftpg.application.notification.InviteEmailSender;
import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionAction;
import com.craftpg.infrastructure.security.campaignpermission.RequireCampaignPermission;
import com.craftpg.infrastructure.web.dto.CreateInviteRequest;
import com.craftpg.shared.constants.CampaignRoleType;
import com.craftpg.shared.util.HashUtil;
import lombok.NonNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateInviteUsecaseImpl implements CreateInviteUsecase {

    private final CampaignInviteRepository campaignInviteRepository;
    private final AppUserRepository appUserRepository;
    private final NotificationRepository notificationRepository;
    private final InviteEmailSender inviteEmailSender;

    @Override
    @Transactional
    @RequireCampaignPermission(action = CampaignPermissionAction.INVITE)
    public String execute(@NonNull final UUID campaignId, @NonNull final CreateInviteRequest command) {
        var token = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        var roles = command.getRoles().stream().map(String::toUpperCase)
            .filter(v -> !"DM".equals(v))
            .map(CampaignRoleType::valueOf)
            .collect(Collectors.toSet());
        if (roles.isEmpty()) {
            roles = Set.of(CampaignRoleType.PLAYER);
        }
        var invite = CampaignInvite.create(campaignId, command.getEmail(), HashUtil.sha256(token), roles, LocalDateTime.now().plusDays(7));
        campaignInviteRepository.save(invite);

        appUserRepository.findByEmailIgnoreCase(command.getEmail())
            .ifPresent(user -> notificationRepository.save(Notification.createInviteNotification(
                user.getId(),
                campaignId,
                command.getEmail(),
                invite.getRolesJson()
            )));

        inviteEmailSender.sendInviteEmail(command.getEmail(), token);
        return token;
    }
}
