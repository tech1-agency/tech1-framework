package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class Tuple3<A, B, C> {
    private final A a;
    private final B b;
    private final C c;
}
