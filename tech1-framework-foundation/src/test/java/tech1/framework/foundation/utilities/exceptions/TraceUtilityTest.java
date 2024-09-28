package tech1.framework.foundation.utilities.exceptions;

import org.junit.jupiter.api.RepeatedTest;

import static tech1.framework.foundation.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static tech1.framework.foundation.utilities.exceptions.TraceUtility.getTrace;
import static org.assertj.core.api.Assertions.assertThat;

class TraceUtilityTest {

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
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
        assertThat(actual.value()).contains("at io.tech1.framework.foundation.utilities.exceptions.TraceUtilityTest.getTraceTest(TraceUtilityTest.java:14)");
    }
}
