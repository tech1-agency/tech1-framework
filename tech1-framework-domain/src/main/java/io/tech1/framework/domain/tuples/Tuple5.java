package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class Tuple5<A, B, C, D, E> {
    private final A a;
    private final B b;
    private final C c;
    private final D d;
    private final E e;
}
