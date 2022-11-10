package io.tech1.framework.domain.utilities.random;

import io.tech1.framework.domain.constants.BigDecimalConstants;
import io.tech1.framework.domain.constants.BigIntegerConstants;
import io.tech1.framework.domain.tests.constants.TestsConstants;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class RandomUtilityTest {

    private static Stream<Arguments> oneTest() {
        return Stream.of(
                Arguments.of(Short.class, 1),
                Arguments.of(Integer.class, 1),
                Arguments.of(Long.class, 1L),
                Arguments.of(Double.class, 1.0d),
                Arguments.of(BigDecimal.class, ONE)
        );
    }

    @ParameterizedTest
    @MethodSource("oneTest")
    public void oneTest(Class<? extends Number> clazz, Number expected) {
        // Act
        var actual = one(clazz);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void oneTestException() {
        // Act
        var thrown = catchThrowable(() -> one(Float.class));

        // Assert
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
        assertThat(thrown).hasMessageContaining("Unexpected clazz: java.lang.Float");
    }

    @Test
    public void randomShortTest() {
        // Act
        var actual = randomShort();

        // Assert
        assertThat(actual).isNotNull();
    }

    @Test
    public void randomBooleanTest() {
        // Act
        var actual = randomBoolean();

        // Assert
        assertThat(actual).isNotNull();
    }

    @Test
    public void randomDoubleTest() {
        // Act
        var actual = randomDouble();

        // Assert
        assertThat(actual).isNotNull();
    }

    @Test
    public void randomIntegerTest() {
        // Act
        var actual = randomInteger();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomIntegerGreaterThanZeroTest() {
        // Act
        var actual = randomIntegerGreaterThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isPositive();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomIntegerLessThanZeroTest() {
        // Act
        var actual = randomIntegerLessThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isNegative();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomIntegerGreaterThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = 50;
        var upperBound = 100;

        // Act
        var actual = randomIntegerGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual > lowerBound).isTrue();
        assertThat(actual < upperBound).isTrue();
    }

    @Test
    public void randomLongTest() {
        // Act
        var actual = randomLong();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomLongGreaterThanZeroTest() {
        // Act
        var actual = randomLongGreaterThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isPositive();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomLongLessThanZeroTest() {
        // Act
        var actual = randomLongLessThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isNegative();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomLongGreaterThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = 50L;
        var upperBound = 100L;

        // Act
        var actual = randomLongGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual > lowerBound).isTrue();
        assertThat(actual < upperBound).isTrue();
    }

    @Test
    public void randomBigDecimalTest() {
        // Act
        var actual = randomBigDecimal();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigDecimalGreaterThanZeroTest() {
        // Act
        var actual = randomBigDecimalGreaterThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigDecimal.ZERO);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigDecimalLessThanZeroTest() {
        // Act
        var actual = randomBigDecimalLessThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isLessThan(BigDecimal.ZERO);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigDecimalGreaterThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = 50L;
        var upperBound = 100L;

        // Act
        var actual = randomBigDecimalGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigDecimal.valueOf(lowerBound));
        assertThat(actual).isLessThan(BigDecimal.valueOf(upperBound));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigDecimalLessThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = 50L;
        var upperBound = 100L;

        // Act
        var actual = randomBigDecimalLessThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigDecimal.valueOf(upperBound).multiply(BigDecimalConstants.MINUS_ONE));
        assertThat(actual).isLessThan(BigDecimal.valueOf(lowerBound).multiply(BigDecimalConstants.MINUS_ONE));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigDecimalByBoundsTest() {
        // Arrange
        var lowerBound = 50L;
        var upperBound = 100L;

        // Act
        var actual = randomBigDecimalByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigDecimal.valueOf(upperBound).multiply(BigDecimalConstants.MINUS_ONE));
        assertThat(actual).isLessThan(BigDecimal.valueOf(upperBound));
    }

    @Test
    public void randomBigIntegerTest() {
        // Act
        var actual = randomBigInteger();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigIntegerGreaterThanZeroTest() {
        // Act
        var actual = randomBigIntegerGreaterThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigInteger.ZERO);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigIntegerLessThanZeroTestt() {
        // Act
        var actual = randomBigIntegerLessThanZero();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isLessThan(BigInteger.ZERO);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigIntegerGreaterThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = 50L;
        var upperBound = 100L;

        // Act
        var actual = randomBigIntegerGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigInteger.valueOf(lowerBound));
        assertThat(actual).isLessThan(BigInteger.valueOf(upperBound));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigIntegerLessThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = 50L;
        var upperBound = 100L;

        // Act
        var actual = randomBigIntegerLessThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigInteger.valueOf(upperBound).multiply(BigIntegerConstants.MINUS_ONE));
        assertThat(actual).isLessThan(BigInteger.valueOf(lowerBound).multiply(BigIntegerConstants.MINUS_ONE));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigIntegerByBoundsTest() {
        // Arrange
        var lowerBound = 50L;
        var upperBound = 100L;

        // Act
        var actual = randomBigIntegerByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThan(BigInteger.valueOf(upperBound).multiply(BigIntegerConstants.MINUS_ONE));
        assertThat(actual).isLessThan(BigInteger.valueOf(upperBound));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomStringTest() {
        // Act
        var actual = randomString();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.length()).isEqualTo(32L);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomStringLetterOrNumbersOnlyTest() {
        // Arrange
        var regex = "[^a-z0-9 ]";
        var size = 40;

        // Act
        var randomStringLetterOrNumbersOnly = randomStringLetterOrNumbersOnly(size);

        // Assert
        assertThat(randomStringLetterOrNumbersOnly).isNotNull();
        assertThat(randomStringLetterOrNumbersOnly.length()).isEqualTo(size);

        var matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(randomStringLetterOrNumbersOnly);
        assertThat(matcher.find()).isFalse();
    }
}
