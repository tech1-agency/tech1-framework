package jbst.foundation.domain.tuples;

public record TupleToggle<A>(boolean enabled, A value) {
    public static <A> TupleToggle<A> enabled(A value) {
        return new TupleToggle<>(true, value);
    }

    public static <A> TupleToggle<A> disabled(A value) {
        return new TupleToggle<>(false, value);
    }

    public static <A> TupleToggle<A> disabled() {
        return new TupleToggle<>(false, null);
    }
}
