package io.tech1.framework.domain.tuples;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Tuple5<A, B, C, D, E> {
    private final A a;
    private final B b;
    private final C c;
    private final D d;
    private final E e;

    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(
            A a,
            B b,
            C c,
            D d,
            E e
    ) {
        return new Tuple5<>(
                a,
                b,
                c,
                d,
                e
        );
    }
}
