package com.craftpg.infrastructure.web.controller;

import lombok.NonNull;

import com.craftpg.application.mapper.InviteMapper;
import com.craftpg.application.usecase.invite.acceptinvite.AcceptInviteUsecase;
import com.craftpg.application.usecase.invite.cancelpendinginvite.CancelPendingInviteUsecase;
import com.craftpg.application.usecase.invite.createinvite.CreateInviteUsecase;
import com.craftpg.application.usecase.invite.getinvitebytoken.GetInviteByTokenUsecase;
import com.craftpg.application.usecase.invite.listpendinginvites.ListPendingInvitesUsecase;
import com.craftpg.infrastructure.web.api.InvitesApi;
import com.craftpg.infrastructure.web.dto.AcceptInviteRequest;
import com.craftpg.infrastructure.web.dto.CampaignIdResponse;
import com.craftpg.infrastructure.web.dto.CreateInviteRequest;
import com.craftpg.infrastructure.web.dto.InvitePreviewResponse;
import com.craftpg.infrastructure.web.dto.PendingInviteResponse;
import com.craftpg.infrastructure.web.dto.TokenResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InviteController implements InvitesApi {

    private final CreateInviteUsecase createInviteUsecase;
    private final ListPendingInvitesUsecase listPendingInvitesUsecase;
    private final CancelPendingInviteUsecase cancelPendingInviteUsecase;
    private final GetInviteByTokenUsecase getInviteByTokenUsecase;
    private final AcceptInviteUsecase acceptInviteUsecase;
    private final InviteMapper inviteMapper;

    @Override
    public ResponseEntity<List<PendingInviteResponse>> campaignsCampaignIdInvitesGet(@NonNull final UUID campaignId) {
        var pendingInvites = listPendingInvitesUsecase.execute(campaignId).stream().map(inviteMapper::toPendingInviteResponse).toList();
        return ResponseEntity.ok(pendingInvites);
    }

    @Override
    public ResponseEntity<TokenResponse> campaignsCampaignIdInvitesPost(@NonNull final UUID campaignId, @NonNull final CreateInviteRequest createInviteRequest) {
        var token = createInviteUsecase.execute(campaignId, createInviteRequest);
        return ResponseEntity.status(201).body(inviteMapper.toTokenResponse(token));
    }

    @Override
    public ResponseEntity<Void> campaignsCampaignIdInvitesInviteIdDelete(@NonNull final UUID campaignId, @NonNull final UUID inviteId) {
        cancelPendingInviteUsecase.execute(campaignId, inviteId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<InvitePreviewResponse> invitesTokenGet(@NonNull final String token) {
        return ResponseEntity.ok(inviteMapper.toPreviewResponse(getInviteByTokenUsecase.execute(token)));
    }

    @Override
    public ResponseEntity<CampaignIdResponse> invitesTokenAcceptPost(@NonNull final String token, @NonNull final AcceptInviteRequest acceptInviteRequest) {
        var campaignId = acceptInviteUsecase.execute(token, acceptInviteRequest.getCharacterBaseId());
        return ResponseEntity.ok(inviteMapper.toCampaignIdResponse(campaignId));
    }
}
