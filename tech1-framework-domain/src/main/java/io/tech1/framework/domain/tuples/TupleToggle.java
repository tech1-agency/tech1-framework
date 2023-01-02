package io.tech1.framework.domain.tuples;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class TupleToggle<A> {
    private final boolean enabled;
    private final A value;

    public static <A> TupleToggle<A> enabled(A value) {
        return TupleToggle.of(true, value);
    }

    public static <A> TupleToggle<A> disabled(A value) {
        return TupleToggle.of(false, value);
    }
}
