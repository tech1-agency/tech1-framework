package tech1.framework.foundation.domain.tuples;

import tech1.framework.foundation.domain.constants.BigDecimalConstants;

import java.math.BigDecimal;

import static tech1.framework.foundation.utilities.numbers.RoundingUtility.divideOrZero;
import static tech1.framework.foundation.utilities.numbers.RoundingUtility.scale;
import static java.math.BigDecimal.ZERO;

public record TuplePercentage(
        BigDecimal value,
        BigDecimal percentage
) {

    public static TuplePercentage of(BigDecimal value, BigDecimal maxValue, int valueScale, int percentageScale) {
        return new TuplePercentage(
                scale(value, valueScale),
                divideOrZero(value.abs().multiply(BigDecimalConstants.ONE_HUNDRED), maxValue, percentageScale)
        );
    }

    public static TuplePercentage progressTuplePercentage(BigDecimal value, BigDecimal maxValue) {
        return of(value, maxValue, 2, 2);
    }

    public static TuplePercentage progressTuplePercentage(long value, long maxValue) {
        return progressTuplePercentage(new BigDecimal(value), new BigDecimal(maxValue));
    }

    public static TuplePercentage zero() {
        return progressTuplePercentage(ZERO, BigDecimalConstants.ONE_HUNDRED);
    }

    public static TuplePercentage oneHundred() {
        return progressTuplePercentage(BigDecimalConstants.ONE_HUNDRED, BigDecimalConstants.ONE_HUNDRED);
    }
}
