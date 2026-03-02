package com.craftpg.application.usecase.invite.createinvite;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.application.notification.InviteEmailSender;
import com.craftpg.domain.model.AppUser;
import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.domain.model.Notification;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.persistence.repository.NotificationRepository;
import com.craftpg.infrastructure.web.dto.CreateInviteRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateInviteUsecaseImplTest {

    @Mock
    private CampaignInviteRepository campaignInviteRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private InviteEmailSender inviteEmailSender;

    @InjectMocks
    private CreateInviteUsecaseImpl usecase;

    @Test
    void execute_userNotFound_createsInviteAndSendsEmailWithoutNotification() {
        // Given
        var campaignId = UUID.randomUUID();
        var command = Instancio.of(CreateInviteRequest.class)
            .set(field(CreateInviteRequest::getEmail), "hero@craftpg.com")
            .set(field(CreateInviteRequest::getRoles), List.of("DM"))
            .create();

        when(appUserRepository.findByEmailIgnoreCase(command.getEmail())).thenReturn(Optional.empty());

        // When
        var token = usecase.execute(campaignId, command);

        // Then
        assertNotNull(token);
        assertEquals(64, token.length());
        verify(campaignInviteRepository).save(any(CampaignInvite.class));
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(inviteEmailSender).sendInviteEmail(command.getEmail(), token);
    }

    @Test
    void execute_userFound_createsNotification() {
        // Given
        var campaignId = UUID.randomUUID();
        var command = Instancio.of(CreateInviteRequest.class)
            .set(field(CreateInviteRequest::getEmail), "hero@craftpg.com")
            .set(field(CreateInviteRequest::getRoles), List.of("player"))
            .create();
        var user = mock(AppUser.class);
        when(user.getId()).thenReturn(UUID.randomUUID());

        when(appUserRepository.findByEmailIgnoreCase(command.getEmail())).thenReturn(Optional.of(user));

        // When
        var token = usecase.execute(campaignId, command);

        // Then
        assertNotNull(token);
        verify(notificationRepository).save(any(Notification.class));
        verify(inviteEmailSender).sendInviteEmail(command.getEmail(), token);
    }
}
