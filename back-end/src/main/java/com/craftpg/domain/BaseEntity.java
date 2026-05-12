package com.craftpg.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity<ID extends TypedId<?>> implements Serializable {

    @Version
    private Long version;

    public abstract ID getId();

    protected void setVersion(Long version) {
        this.version = version;
    }
}