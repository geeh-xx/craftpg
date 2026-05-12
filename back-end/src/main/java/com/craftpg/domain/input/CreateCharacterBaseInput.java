package com.craftpg.domain.input;

import java.util.UUID;

public record CreateCharacterBaseInput(UUID ownerUserId, String name, String race, String clazz, String attributesJson
) {
}
