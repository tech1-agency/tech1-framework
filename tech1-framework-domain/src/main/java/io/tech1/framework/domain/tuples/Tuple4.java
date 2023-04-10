package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class Tuple4<A, B, C, D> {
    private final A a;
    private final B b;
    private final C c;
    private final D d;
}
