package io.tech1.framework.domain.tuples;

import lombok.Data;

import java.math.BigDecimal;

import static io.tech1.framework.domain.constants.BigDecimalConstants.ONE_HUNDRED;
import static io.tech1.framework.domain.utilities.numbers.RoundingUtility.divideOrZero;
import static io.tech1.framework.domain.utilities.numbers.RoundingUtility.scale;
import static java.math.BigDecimal.ZERO;

// Lombok
@Data(staticConstructor = "of")
public class TuplePercentage {
    private final BigDecimal value;
    private final BigDecimal percentage;

    public static TuplePercentage of(BigDecimal value, BigDecimal maxValue, int valueScale, int percentageScale) {
        return TuplePercentage.of(
                scale(value, valueScale),
                divideOrZero(value.abs().multiply(ONE_HUNDRED), maxValue, percentageScale)
        );
    }

    public static TuplePercentage progressTuplePercentage(BigDecimal value, BigDecimal maxValue) {
        return of(value, maxValue, 2, 2);
    }

    public static TuplePercentage progressTuplePercentage(long value, long maxValue) {
        return progressTuplePercentage(new BigDecimal(value), new BigDecimal(maxValue));
    }

    public static TuplePercentage zero() {
        return progressTuplePercentage(ZERO, ONE_HUNDRED);
    }

    public static TuplePercentage oneHundred() {
        return progressTuplePercentage(ONE_HUNDRED, ONE_HUNDRED);
    }
}
