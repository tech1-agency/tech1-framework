package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class TuplePresence<A> {
    private final boolean present;
    private final A value;

    public static <A> TuplePresence<A> present(A value) {
        return TuplePresence.of(true, value);
    }

    public static <A> TuplePresence<A> absent(A value) {
        return TuplePresence.of(false, value);
    }
}
