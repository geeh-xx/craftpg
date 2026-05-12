package com.craftpg.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AggregateTypedId implements TypedId<UUID> {

    @Column(name = "id", nullable = false, updatable = false)
    private UUID value;

    protected AggregateTypedId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    protected static UUID newUuid() {
        return UUID.randomUUID();
    }

    @Override
    public UUID unwrap() {
        return value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregateTypedId that = (AggregateTypedId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
