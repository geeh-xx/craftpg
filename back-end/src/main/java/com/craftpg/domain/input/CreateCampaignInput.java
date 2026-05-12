package com.craftpg.domain.input;


import java.util.UUID;

public record CreateCampaignInput(UUID createBy, String title,
                                  String description, String frequency, String status, Integer progressPercent
) {
}
