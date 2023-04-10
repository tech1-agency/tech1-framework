package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class TupleRange<T> {
    private final T from;
    private final T to;
}
