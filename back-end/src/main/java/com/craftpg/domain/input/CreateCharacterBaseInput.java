package com.craftpg.domain.input;

import java.util.UUID;
import org.jspecify.annotations.NonNull;

public record CreateCharacterBaseInput(
    @NonNull UUID ownerUserId,
    @NonNull String name,
    @NonNull String race,
    @NonNull String clazz,
    @NonNull String attributesJson
) {
}
