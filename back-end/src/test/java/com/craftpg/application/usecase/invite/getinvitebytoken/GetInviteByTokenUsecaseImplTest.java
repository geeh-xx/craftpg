package com.craftpg.application.usecase.invite.getinvitebytoken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.infrastructure.exception.ApiException;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.shared.util.HashUtil;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetInviteByTokenUsecaseImplTest {

    @Mock
    private CampaignInviteRepository campaignInviteRepository;

    @InjectMocks
    private GetInviteByTokenUsecaseImpl usecase;

    @Test
    void execute_existingToken_returnsInvite() {
        // Given
        var token = "token-value";
        var invite = mock(CampaignInvite.class);

        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.of(invite));

        // When
        var result = usecase.execute(token);

        // Then
        assertSame(invite, result);
        verify(campaignInviteRepository).findByTokenHash(HashUtil.sha256(token));
    }

    @Test
    void execute_missingToken_throwsApiException() {
        // Given
        var token = "token-value";
        when(campaignInviteRepository.findByTokenHash(HashUtil.sha256(token))).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(ApiException.class, () -> usecase.execute(token));

        // Then
        assertEquals("invite not found", exception.getMessage());
    }
}
