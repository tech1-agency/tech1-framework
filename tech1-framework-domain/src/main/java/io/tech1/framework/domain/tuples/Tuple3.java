package io.tech1.framework.domain.tuples;

import lombok.*;

// Lombok
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Tuple3<A, B, C> {
    private final A a;
    private final B b;
    private final C c;

    public static <A, B, C> Tuple3<A, B, C> of(
            A a,
            B b,
            C c
    ) {
        return new Tuple3<>(
                a,
                b,
                c
        );
    }
}