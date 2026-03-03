package com.craftpg.domain.input;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.NonNull;

public record CreateCampaignSessionInput(
    @NonNull UUID campaignId,
    @NonNull String title,
    @NonNull LocalDateTime scheduledAt,
    @NonNull String summary,
    @NonNull String notes,
    @NonNull String attendanceJson,
    @NonNull String xpJson,
    @NonNull String npcsJson,
    @NonNull String mapsJson,
    @NonNull String treasuresJson
) {
}
