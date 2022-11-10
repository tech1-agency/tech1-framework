package io.tech1.framework.domain.tuples;

import lombok.*;

// Lombok
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Tuple1<A> {
    private final A value;

    public static <A> Tuple1<A> of(
            A value
    ) {
        return new Tuple1<>(value);
    }
}
