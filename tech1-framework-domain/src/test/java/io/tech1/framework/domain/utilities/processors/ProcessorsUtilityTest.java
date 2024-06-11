package io.tech1.framework.domain.utilities.processors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static io.tech1.framework.domain.tuples.TuplePercentage.progressTuplePercentage;
import static io.tech1.framework.domain.utilities.processors.ProcessorsUtility.getHalfOfCores;
import static io.tech1.framework.domain.utilities.processors.ProcessorsUtility.getNumOfCores;
import static org.assertj.core.api.Assertions.assertThat;

class ProcessorsUtilityTest {

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void getNumOfCoresTest() {
        // Act
        int cores = getNumOfCores();

        // Assert
        assertThat(cores).isPositive();
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void getHalfOfCoresTest() {
        // Act
        int cores = getHalfOfCores();

        // Assert
        assertThat(cores).isPositive();
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void integrationComparisonTest() {
        // Act
        int numOfCores = getNumOfCores();
        int halfOfCores = getHalfOfCores();

        // Assert
        assertThat(numOfCores).isPositive();
        assertThat(halfOfCores).isEqualTo(numOfCores * 5 / 10);
    }

    @Disabled
    @Test
    public void devTest() {
        // Arrange
        var _88 = progressTuplePercentage(new BigDecimal("0.88"), BigDecimal.ONE);
        var _875 = progressTuplePercentage(new BigDecimal("0.875"), BigDecimal.ONE);
        var _87 = progressTuplePercentage(new BigDecimal("0.87"), BigDecimal.ONE);
        var _75 = progressTuplePercentage(new BigDecimal("0.75"), BigDecimal.ONE);
        var _25 = progressTuplePercentage(new BigDecimal("0.25"), BigDecimal.ONE);
        var _20 = progressTuplePercentage(new BigDecimal("0.20"), BigDecimal.ONE);
        var _15 = progressTuplePercentage(new BigDecimal("0.15"), BigDecimal.ONE);
        var _125 = progressTuplePercentage(new BigDecimal("0.125"), BigDecimal.ONE);
        var _124 = progressTuplePercentage(new BigDecimal("0.124"), BigDecimal.ONE);

        // Act
        System.out.println("numOfCores [_100]: " + getNumOfCores());
        System.out.println("numOfCores [_88]: " + getNumOfCores(_88));
        System.out.println("numOfCores [_875]: " + getNumOfCores(_875));
        System.out.println("numOfCores [_87]: " + getNumOfCores(_87));
        System.out.println("numOfCores [_75]: " + getNumOfCores(_75));
        System.out.println("numOfCores [_25]: " + getNumOfCores(_25));
        System.out.println("numOfCores [_20]: " + getNumOfCores(_20));
        System.out.println("numOfCores [_15]: " + getNumOfCores(_15));
        System.out.println("numOfCores [_125]: " + getNumOfCores(_125));
        System.out.println("numOfCores [_124]: " + getNumOfCores(_124));
    }
}
