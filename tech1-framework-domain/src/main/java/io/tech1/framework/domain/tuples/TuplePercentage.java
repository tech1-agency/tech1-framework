package io.tech1.framework.domain.tuples;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.constants.BigDecimalConstants.ONE_HUNDRED;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.numbers.RoundingUtility.divideOrZero;
import static io.tech1.framework.domain.utilities.numbers.RoundingUtility.scale;
import static java.math.BigDecimal.ZERO;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class TuplePercentage {
    private final BigDecimal value;
    private final BigDecimal percentage;

    private TuplePercentage(
            BigDecimal value,
            BigDecimal percentage
    ) {
        assertNonNullOrThrow(value, invalidAttribute("TuplePercentage.value"));
        assertNonNullOrThrow(percentage, invalidAttribute("TuplePercentage.percentage"));
        this.value = value;
        this.percentage = percentage;
    }

    public static TuplePercentage of(BigDecimal value, BigDecimal maxValue, int valueScale, int percentageScale) {
        return new TuplePercentage(
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
