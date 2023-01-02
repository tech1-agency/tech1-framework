package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class Tuple1<A> {
    private final A value;
}
