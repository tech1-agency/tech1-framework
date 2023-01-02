package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class Tuple6<A, B, C, D, E, F> {
    private final A a;
    private final B b;
    private final C c;
    private final D d;
    private final E e;
    private final F f;
}
