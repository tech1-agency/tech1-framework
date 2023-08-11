package io.tech1.framework.domain.utilities.processors;

import org.junit.jupiter.api.RepeatedTest;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
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
}
