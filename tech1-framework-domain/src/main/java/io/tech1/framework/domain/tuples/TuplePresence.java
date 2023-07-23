package io.tech1.framework.domain.tuples;

public record TuplePresence<A>(boolean present, A value) {
    public static <A> TuplePresence<A> present(A value) {
        return new TuplePresence<>(true, value);
    }

    public static <A> TuplePresence<A> absent(A value) {
        return new TuplePresence<>(false, value);
    }
}
