package com.craftpg.domain.event;

import com.craftpg.domain.model.campaign.CampaignID;

import java.time.Instant;

public record CreateCampaignEvent(CampaignID id, Instant occurred)
        implements DomainEvent<CampaignID> {

    @Override
    public CampaignID getAggregateID() {
        return id;
    }

    @Override
    public Instant occurredOn() {
        return occurred;
    }
}
