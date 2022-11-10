package io.tech1.framework.domain.tuples;

import lombok.*;

// Lombok
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Tuple6<A, B, C, D, E, F> {
    private final A a;
    private final B b;
    private final C c;
    private final D d;
    private final E e;
    private final F f;

    public static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> of(
            A a,
            B b,
            C c,
            D d,
            E e,
            F f
    ) {
        return new Tuple6<>(
                a,
                b,
                c,
                d,
                e,
                f
        );
    }
}
