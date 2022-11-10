package io.tech1.framework.domain.tuples;

import lombok.*;

// Lombok
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Tuple4<A, B, C, D> {
    private final A a;
    private final B b;
    private final C c;
    private final D d;

    public static <A, B, C, D> Tuple4<A, B, C, D> of(
            A a,
            B b,
            C c,
            D d
    ) {
        return new Tuple4<>(
                a,
                b,
                c,
                d
        );
    }
}
