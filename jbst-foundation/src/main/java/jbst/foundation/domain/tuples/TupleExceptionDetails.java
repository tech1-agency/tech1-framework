package jbst.foundation.domain.tuples;

import lombok.Data;

// Lombok
@Data
public class TupleExceptionDetails {
    private final boolean ok;
    private final String message;

    public static TupleExceptionDetails ok() {
        return new TupleExceptionDetails(
                true,
                ""
        );
    }

    public static TupleExceptionDetails exception(
            String message
    ) {
        return new TupleExceptionDetails(
                false,
                message
        );
    }
}
