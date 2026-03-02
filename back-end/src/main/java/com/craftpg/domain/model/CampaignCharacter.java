package com.craftpg.domain.model;

import lombok.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "campaign_character")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignCharacter {

    @Id
    private UUID id;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(name = "character_base_id", nullable = false)
    private UUID characterBaseId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Integer xp;

    @Column(name = "sheet_state_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String sheetStateJson;

    @Column(name = "inventory_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String inventoryJson;

    @Column(nullable = false)
    private Boolean locked;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static CampaignCharacter fromBase(@NonNull final UUID campaignId, @NonNull final UUID userId, @NonNull final UUID characterBaseId) {
        var character = new CampaignCharacter();
        character.id = UUID.randomUUID();
        character.campaignId = campaignId;
        character.userId = userId;
        character.characterBaseId = characterBaseId;
        character.level = 1;
        character.xp = 0;
        character.sheetStateJson = "{}";
        character.inventoryJson = "[]";
        character.locked = true;
        character.createdAt = LocalDateTime.now();
        return character;
    }

    public void updateSheet(@NonNull final String sheetStateJson, @NonNull final String inventoryJson) {
        this.sheetStateJson = sheetStateJson;
        this.inventoryJson = inventoryJson;
    }

    public void addXpAndApplyHappyPathLevelUp(@NonNull final Integer gainedXp) {
        if (gainedXp < 0) {
            throw new IllegalArgumentException("gainedXp cannot be negative");
        }
        this.xp = this.xp + gainedXp;
        while (this.xp >= this.level * 1_000) {
            this.level = this.level + 1;
        }
    }
}
