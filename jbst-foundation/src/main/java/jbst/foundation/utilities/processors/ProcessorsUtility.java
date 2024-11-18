package jbst.foundation.utilities.processors;

import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.tuples.TuplePercentage;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class ProcessorsUtility {
    public static int getNumOfCores(TuplePercentage tuplePercentage) {
        return BigDecimal.valueOf(getNumOfCores()).multiply(tuplePercentage.percentage()).divide(
                JbstConstants.BigDecimals.ONE_HUNDRED,
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
