package jbst.foundation.utilities.exceptions;

import org.junit.jupiter.api.RepeatedTest;

import static jbst.foundation.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static jbst.foundation.utilities.exceptions.TraceUtility.getTrace;
import static org.assertj.core.api.Assertions.assertThat;

class TraceUtilityTest {

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void getTraceTest() {
        // Arrange
        var npe = new NullPointerException("jbst");

        // Act
        var actual = getTrace(npe);

        // Arrange
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isNotNull();
        assertThat(actual.value().length()).isGreaterThan(10000);
        assertThat(actual.value()).startsWith("java.lang.NullPointerException: jbst");
        assertThat(actual.value()).contains("at jbst.foundation.utilities.exceptions.TraceUtilityTest.getTraceTest(TraceUtilityTest.java:14)");
    }
}
