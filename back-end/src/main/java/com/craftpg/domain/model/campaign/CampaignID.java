package com.craftpg.domain.model.campaign;

import com.craftpg.domain.AggregateTypedId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignID extends AggregateTypedId {

    protected CampaignID(UUID value) {
        super(value);
    }

    public static CampaignID generate() {
        return new CampaignID(newUuid());
    }

    public static CampaignID of(@NonNull final UUID value) {
        return new CampaignID(value);
    }
}
