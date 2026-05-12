package com.craftpg.application.listener;

import com.craftpg.domain.event.CreateCampaignEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CampaignCreatedEventListener implements DomainEventListener<CreateCampaignEvent> {

    @EventListener
    public void handle(CreateCampaignEvent event) {
    }
}
