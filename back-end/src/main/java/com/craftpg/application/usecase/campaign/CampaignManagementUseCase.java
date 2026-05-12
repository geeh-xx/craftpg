package com.craftpg.application.usecase.campaign;

import com.craftpg.application.usecase.OperationResult;
import com.craftpg.application.usecase.UseCaseManagement;
import com.craftpg.infrastructure.web.dto.CampaignResponse;
import com.craftpg.infrastructure.web.dto.CreateCampaignRequest;
import com.craftpg.infrastructure.web.dto.UpdateCampaignRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CampaignManagementUseCase
        extends UseCaseManagement<CampaignResponse, OperationResult<CampaignResponse>, CreateCampaignRequest,
        UpdateCampaignRequest, Pageable, UUID> {
}
