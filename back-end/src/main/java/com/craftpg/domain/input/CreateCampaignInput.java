package com.craftpg.domain.input;


import org.jspecify.annotations.NonNull;

public record CreateCampaignInput(
    @NonNull String title,
    String description,
    @NonNull String frequency,
    @NonNull String status,
    @NonNull Integer progressPercent
) {
}
