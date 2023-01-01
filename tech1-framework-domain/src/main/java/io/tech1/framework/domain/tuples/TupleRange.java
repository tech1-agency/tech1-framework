package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class TupleRange<T> {
    private final T from;
    private final T to;
}
