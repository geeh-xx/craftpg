package com.craftpg.infrastructure.web.controller;

import com.craftpg.application.mapper.InviteMapper;
import com.craftpg.application.usecase.invite.acceptinvite.AcceptInviteUsecase;
import com.craftpg.application.usecase.invite.cancelpendinginvite.CancelPendingInviteUsecase;
import com.craftpg.application.usecase.invite.createinvite.CreateInviteUsecase;
import com.craftpg.application.usecase.invite.getinvitebytoken.GetInviteByTokenUsecase;
import com.craftpg.application.usecase.invite.listpendinginvites.ListPendingInvitesUsecase;
import com.craftpg.infrastructure.factory.UseCaseProvider;
import com.craftpg.infrastructure.web.api.InvitesApi;
import com.craftpg.infrastructure.web.dto.AcceptInviteRequest;
import com.craftpg.infrastructure.web.dto.CampaignIdResponse;
import com.craftpg.infrastructure.web.dto.CreateInviteRequest;
import com.craftpg.infrastructure.web.dto.InvitePreviewResponse;
import com.craftpg.infrastructure.web.dto.PendingInviteResponse;
import com.craftpg.infrastructure.web.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InviteController implements InvitesApi {

    private final UseCaseProvider useCaseProvider;
    private final InviteMapper inviteMapper;

    @Override
    public ResponseEntity<List<PendingInviteResponse>> getPendingInvitesByCampaignId(final UUID campaignId) {
        var pendingInvites = useCaseProvider.getUseCase(ListPendingInvitesUsecase.class)
                .execute(campaignId)
                .stream()
                .map(inviteMapper::toPendingInviteResponse)
                .toList();
        return ResponseEntity.ok(pendingInvites);
    }

    @Override
    public ResponseEntity<TokenResponse> createInviteByCampaignId(final UUID campaignId, final CreateInviteRequest createInviteRequest) {
        var token = useCaseProvider.getUseCase(CreateInviteUsecase.class).execute(campaignId, createInviteRequest);
        return ResponseEntity.status(201).body(inviteMapper.toTokenResponse(token));
    }

    @Override
    public ResponseEntity<Void> cancelInviteByCampaignIdAndInviteId(final UUID campaignId, final UUID inviteId) {
        useCaseProvider.getUseCase(CancelPendingInviteUsecase.class).execute(campaignId, inviteId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<InvitePreviewResponse> getInvitePreviewByToken(final String token) {
        var previewData = useCaseProvider.getUseCase(GetInviteByTokenUsecase.class).execute(token);
        return ResponseEntity.ok(inviteMapper.toPreviewResponse(previewData));
    }

    @Override
    public ResponseEntity<CampaignIdResponse> acceptInviteByToken(final String token, final AcceptInviteRequest acceptInviteRequest) {
        var campaignId = useCaseProvider.getUseCase(AcceptInviteUsecase.class).execute(token, acceptInviteRequest.getCharacterBaseId());
        return ResponseEntity.ok(inviteMapper.toCampaignIdResponse(campaignId));
    }
}
