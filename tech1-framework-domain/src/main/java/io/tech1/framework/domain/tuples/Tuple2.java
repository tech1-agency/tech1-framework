package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class Tuple2<A, B> {
    private final A a;
    private final B b;
}
