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
public class Tuple1<A> {
    private final A value;

    public static <A> Tuple1<A> of(
            A value
    ) {
        return new Tuple1<>(value);
    }
}
