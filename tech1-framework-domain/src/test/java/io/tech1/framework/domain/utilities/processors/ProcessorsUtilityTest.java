package io.tech1.framework.domain.utilities.processors;

import io.tech1.framework.domain.tests.constants.TestsConstants;
import org.junit.jupiter.api.RepeatedTest;

import static io.tech1.framework.domain.utilities.processors.ProcessorsUtility.getHalfOfCores;
import static io.tech1.framework.domain.utilities.processors.ProcessorsUtility.getNumOfCores;
import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorsUtilityTest {

    @RepeatedTest(TestsConstants.SMALL_ITERATIONS_COUNT)
    public void getNumOfCoresTest() {
        // Act
        int cores = getNumOfCores();

        // Assert
        assertThat(cores).isPositive();
    }

    @RepeatedTest(TestsConstants.SMALL_ITERATIONS_COUNT)
    public void getHalfOfCoresTest() {
        // Act
        int cores = getHalfOfCores();

        // Assert
        assertThat(cores).isPositive();
    }

    @RepeatedTest(TestsConstants.SMALL_ITERATIONS_COUNT)
    public void integrationComparisonTest() {
        // Act
        int numOfCores = getNumOfCores();
        int halfOfCores = getHalfOfCores();

        // Assert
        assertThat(numOfCores).isPositive();
        assertThat(halfOfCores).isPositive();
        assertThat(halfOfCores).isEqualTo(numOfCores * 5 / 10);
    }
}
