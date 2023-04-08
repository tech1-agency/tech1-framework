package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class TupleToggle<A> {
    private final boolean enabled;
    private final A value;

    public static <A> TupleToggle<A> enabled(A value) {
        return new TupleToggle<>(true, value);
    }

    public static <A> TupleToggle<A> disabled(A value) {
        return new TupleToggle<>(false, value);
    }
}
