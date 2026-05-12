package com.craftpg.application.usecase.campaign;

import com.craftpg.application.mapper.CampaignMapper;
import com.craftpg.domain.input.CreateCampaignInput;
import com.craftpg.domain.model.campaign.Campaign;
import com.craftpg.domain.model.campaign.CampaignID;
import com.craftpg.domain.model.campaign.CampaignRole;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.infrastructure.security.campaignpermission.CampaignPermissionChecker;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignManagementUseCaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignRoleRepository campaignRoleRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private CampaignPermissionChecker campaignPermissionChecker;

    @Mock
    private CampaignMapper campaignMapper;

    @InjectMocks
    private CampaignManagementUseCaseImpl useCase;

    @Test
    void ShouldReturnCampaignResponse_WhenCreateWithValidRequest() {
        // Given
        var request = Instancio.create(CreateCampaignRequest.class);
        var userId = UUID.randomUUID();
        var createInput = new CreateCampaignInput(
                userId,
                request.getTitle(),
                request.getDescription(),
                request.getFrequency(),
                request.getStatus(),
                request.getProgressPercent());
        var campaign = Campaign.create(createInput);
        var response = Instancio.create(CampaignResponse.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignMapper.toCreateInput(request, userId)).thenReturn(createInput);
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);
        when(campaignMapper.toResponse(campaign)).thenReturn(response);

        // When
        var result = useCase.create(request);

        // Then
        assertThat(result.isNotSuccess()).isFalse();
        assertThat(result.getValue()).contains(response);
        verify(campaignRoleRepository).save(any(CampaignRole.class));
    }

    @Test
    void ShouldReturnFailure_WhenUpdateWithoutPermission() {
        // Given
        var request = Instancio.create(UpdateCampaignRequest.class);
        var campaignId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        request.setCampaignId(campaignId);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignPermissionChecker.canEditCampaign(campaignId, userId)).thenReturn(false);

        // When
        var result = useCase.update(request);

        // Then
        assertThat(result.isNotSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("forbidden");
    }

    @Test
    void ShouldReturnFailure_WhenUpdateWithMissingCampaign() {
        // Given
        var request = Instancio.create(UpdateCampaignRequest.class);
        var campaignId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        request.setCampaignId(campaignId);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignPermissionChecker.canEditCampaign(campaignId, userId)).thenReturn(true);
        when(campaignRepository.findById(CampaignID.of(campaignId))).thenReturn(Optional.empty());

        // When
        var result = useCase.update(request);

        // Then
        assertThat(result.isNotSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("campaign not found");
    }

    @Test
    void ShouldReturnCampaignResponse_WhenFindByIdWithExistingCampaign() {
        // Given
        var campaignId = UUID.randomUUID();
        var campaign = mock(Campaign.class);
        var response = Instancio.create(CampaignResponse.class);

        when(campaignRepository.findById(CampaignID.of(campaignId))).thenReturn(Optional.of(campaign));
        when(campaignMapper.toResponse(campaign)).thenReturn(response);

        // When
        var result = useCase.findById(campaignId);

        // Then
        assertThat(result.isNotSuccess()).isFalse();
        assertThat(result.getValue()).contains(response);
    }

    @Test
    void ShouldReturnPaginatedResponse_WhenFindAllWithPageable() {
        // Given
        var pageable = PageRequest.of(0, 10);
        var userId = UUID.randomUUID();
        var campaign = mock(Campaign.class);
        var response = Instancio.create(CampaignResponse.class);
        var page = new PageImpl<>(java.util.List.of(campaign), pageable, 1);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(campaignRepository.findPageByUserId(userId, pageable)).thenReturn(page);
        when(campaignMapper.toResponse(campaign)).thenReturn(response);

        // When
        var result = useCase.findAll(pageable);

        // Then
        assertThat(result.isNotSuccess()).isFalse();
        assertThat(result.getValue()).isPresent();
        assertThat(result.getValue().get().getContent()).containsExactly(response);
    }

    @Test
    void ShouldReturnFailure_WhenDeleteWithMissingCampaign() {
        // Given
        var campaignId = UUID.randomUUID();
        when(campaignRepository.existsById(CampaignID.of(campaignId))).thenReturn(false);

        // When
        var result = useCase.delete(campaignId);

        // Then
        assertThat(result.isNotSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("campaign not found");
    }
}
