package com.craftpg.domain.input;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateCampaignSessionInput(UUID campaignId, String title, LocalDateTime scheduledAt, String summary,
                                         String notes, String attendanceJson, String xpJson, String npcsJson,
                                         String mapsJson, String treasuresJson
) {
}
