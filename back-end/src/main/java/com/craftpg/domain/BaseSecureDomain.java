package com.craftpg.domain;

import java.util.UUID;

public interface BaseSecureDomain<ID> {
    ID getId();

    UUID getOwnerUserId();
}
