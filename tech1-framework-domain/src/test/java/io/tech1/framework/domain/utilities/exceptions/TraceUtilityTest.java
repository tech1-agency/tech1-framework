package io.tech1.framework.domain.utilities.exceptions;

import io.tech1.framework.domain.tests.constants.TestsConstants;
import org.junit.jupiter.api.RepeatedTest;

import static io.tech1.framework.domain.utilities.exceptions.TraceUtility.getTrace;
import static org.assertj.core.api.Assertions.assertThat;

class TraceUtilityTest {

    @RepeatedTest(TestsConstants.SMALL_ITERATIONS_COUNT)
    void getTraceTest() {
        // Arrange
        var npe = new NullPointerException("Tech1");

        // Act
        var actual = getTrace(npe);

        // Arrange
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isNotNull();
        assertThat(actual.value().length()).isGreaterThan(10000);
        assertThat(actual.value()).startsWith("java.lang.NullPointerException: Tech1");
        assertThat(actual.value()).contains("at io.tech1.framework.domain.utilities.exceptions.TraceUtilityTest.getTraceTest(TraceUtilityTest.java:14)");
    }
}
