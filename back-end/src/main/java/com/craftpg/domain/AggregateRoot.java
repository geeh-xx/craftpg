package com.craftpg.domain;

import com.craftpg.domain.event.DomainEvent;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AggregateRoot<ID extends TypedId<?>> extends BaseEntity<ID> {

    @Transient
    private final List<DomainEvent<?>> domainEvents = new ArrayList<>();

    protected <T extends DomainEvent<?>> T registerEvent(T event) {
        domainEvents.add(event);
        return event;
    }

    protected Collection<DomainEvent<?>> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @DomainEvents
    protected Collection<DomainEvent<?>> domainEvents() {
        return getDomainEvents();
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        domainEvents.clear();
    }
}
