package com.craftpg.application.usecase.character;

import com.craftpg.application.usecase.OperationResult;
import com.craftpg.application.usecase.UseCaseManagement;
import com.craftpg.infrastructure.web.dto.CharacterResponse;
import com.craftpg.infrastructure.web.dto.CreateCharacterRequest;
import com.craftpg.infrastructure.web.dto.UpdateCharacterRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CharacterManagementUseCase
        extends UseCaseManagement<CharacterResponse, OperationResult<CharacterResponse>, CreateCharacterRequest,
        UpdateCharacterRequest, Pageable, UUID> {
}
