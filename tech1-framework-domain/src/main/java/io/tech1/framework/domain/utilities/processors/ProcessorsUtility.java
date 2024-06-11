package io.tech1.framework.domain.utilities.processors;

import io.tech1.framework.domain.constants.BigDecimalConstants;
import io.tech1.framework.domain.tuples.TuplePercentage;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class ProcessorsUtility {
    public static int getNumOfCores(TuplePercentage tuplePercentage) {
        return BigDecimal.valueOf(getNumOfCores()).multiply(tuplePercentage.percentage()).divide(
                BigDecimalConstants.ONE_HUNDRED,
                0,
                RoundingMode.DOWN
        ).intValue();
    }

    public static int getNumOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static int getHalfOfCores() {
        return Runtime.getRuntime().availableProcessors() * 5 / 10;
    }

}
