package com.craftpg.domain.event;

import com.craftpg.domain.TypedId;

import java.io.Serializable;
import java.time.Instant;

public interface DomainEvent<ID extends TypedId<?>> extends Serializable {
    ID getAggregateID();

    Instant occurredOn();
}
