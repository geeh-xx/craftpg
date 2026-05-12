package com.craftpg.application.listener;

import com.craftpg.domain.event.DomainEvent;


public interface DomainEventListener<T extends DomainEvent<?>> {
    void handle(T event);
}
