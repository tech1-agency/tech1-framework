package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class TuplePresence<A> {
    private final boolean present;
    private final A value;

    public static <A> TuplePresence<A> present(A value) {
        return new TuplePresence<>(true, value);
    }

    public static <A> TuplePresence<A> absent(A value) {
        return new TuplePresence<>(false, value);
    }
}
