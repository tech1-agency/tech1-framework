package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class Tuple2<A, B> {
    private final A a;
    private final B b;
}
