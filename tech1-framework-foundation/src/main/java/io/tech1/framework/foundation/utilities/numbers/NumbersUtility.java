package io.tech1.framework.foundation.utilities.numbers;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

import static io.tech1.framework.foundation.utilities.numbers.BigDecimalUtility.*;
import static io.tech1.framework.foundation.utilities.numbers.RoundingUtility.divide;
import static io.tech1.framework.foundation.utilities.numbers.RoundingUtility.scale;
import static java.math.BigDecimal.ZERO;
import static java.util.Objects.isNull;

@UtilityClass
public class NumbersUtility {
    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);
    private static final BigDecimal MILLION = BigDecimal.valueOf(1000000);
    private static final BigDecimal BILLION = BigDecimal.valueOf(1000000000);

    public String getReadableNumber(BigDecimal number) {
        return getReadableNumber(number, 2);
    }

    public String getReadableNumber(BigDecimal number, int scale) {
        // N == 0.0
        if (isNull(number) || isZero(number)) {
            return scale(ZERO, scale).toString();
        }
        var positiveNumber = absOrZero(number);
        // -1K < N < 1K
        // N =< -1B
        // N >= 1B
        if (isFirstValueLesser(positiveNumber, THOUSAND) || isFirstValueGreaterOrEqual(positiveNumber, BILLION)) {
            return scale(number, scale).toString();
        }
        // N >= 1M
        if (isFirstValueGreaterOrEqual(positiveNumber, MILLION)) {
            return scale(divide(number, MILLION), 2).toString() + "M";
        }
        // N >= 1K
        return scale(divide(number, THOUSAND), 2).toString() + "K";
    }
}
