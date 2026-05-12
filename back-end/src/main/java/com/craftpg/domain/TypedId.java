package com.craftpg.domain;

import java.io.Serializable;

public interface TypedId<T> extends Serializable {
    T unwrap();
}
