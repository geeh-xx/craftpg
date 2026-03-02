package com.craftpg.application.usecase.campaign.listcampaign;

import com.craftpg.domain.model.Campaign;
import java.util.List;

public interface ListCampaignUsecase {

    List<Campaign> execute();
}
