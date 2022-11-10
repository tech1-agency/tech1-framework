package io.tech1.framework.domain.tuples;

import lombok.*;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class TupleRange<T> {
    private final T from;
    private final T to;

    public static <T> TupleRange<T> of(
            T from,
            T to
    ) {
        assertNonNullOrThrow(from, invalidAttribute("TupleRange.from"));
        assertNonNullOrThrow(to, invalidAttribute("TupleRange.to"));
        return new TupleRange<>(
                from,
                to
        );
    }
}
