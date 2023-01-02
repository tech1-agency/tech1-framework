package io.tech1.framework.domain.tuples;

import io.tech1.framework.domain.constants.StringConstants;
import lombok.*;

// Lombok
@Data(staticConstructor = "of")
public class TupleExceptionDetails {
    private final boolean ok;
    private final String message;

    public static TupleExceptionDetails ok() {
        return TupleExceptionDetails.of(
                true,
                StringConstants.EMPTY
        );
    }

    public static TupleExceptionDetails exception(
            String message
    ) {
        return TupleExceptionDetails.of(
                false,
                message
        );
    }
}
