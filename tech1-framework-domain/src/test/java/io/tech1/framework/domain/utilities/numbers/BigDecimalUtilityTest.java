package io.tech1.framework.domain.utilities.numbers;

import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.tuples.TupleRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.tech1.framework.domain.constants.BigDecimalConstants.ONE_HUNDRED;
import static io.tech1.framework.domain.utilities.numbers.BigDecimalUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBigDecimalGreaterThanZero;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBigDecimalLessThanZero;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class BigDecimalUtilityTest {

    private static Stream<Arguments> areValuesEqualsTest() {
        return Stream.of(
                Arguments.of(null, null, true),
                Arguments.of(null, ZERO, false),
                Arguments.of(ZERO, null, false),
                Arguments.of(ZERO, ZERO, true),
                Arguments.of(ZERO, new BigDecimal("0"), true),
                Arguments.of(ZERO, new BigDecimal("0.00"), true),
                Arguments.of(ZERO, new BigDecimal(0L), true),
                Arguments.of(ZERO, new BigDecimal("1"), false),
                Arguments.of(ZERO, new BigDecimal("1.0"), false)
        );
    }

    private static Stream<Arguments> isFirstValueGreaterExceptionTest() {
        return Stream.of(
                Arguments.of(null, null, "Attribute `number1` is invalid"),
                Arguments.of(null, ZERO, "Attribute `number1` is invalid"),
                Arguments.of(ZERO, null, "Attribute `number2` is invalid")
        );
    }

    private static Stream<Arguments> isFirstValueGreaterTest() {
        return Stream.of(
                Arguments.of(ZERO, new BigDecimal("-1"), true),
                Arguments.of(ZERO, new BigDecimal("1"), false),
                Arguments.of(ZERO, ZERO, false)
        );
    }

    private static Stream<Arguments> isFirstValueGreaterOrEqualExceptionTest() {
        return Stream.of(
                Arguments.of(null, null, "Attribute `number1` is invalid"),
                Arguments.of(null, ZERO, "Attribute `number1` is invalid"),
                Arguments.of(ZERO, null, "Attribute `number2` is invalid")
        );
    }

    private static Stream<Arguments> isFirstValueGreaterOrEqualTest() {
        return Stream.of(
                Arguments.of(ZERO, new BigDecimal("-1"), true),
                Arguments.of(ZERO, new BigDecimal("1"), false),
                Arguments.of(ZERO, ZERO, true)
        );
    }

    private static Stream<Arguments> isFirstValueLesserExceptionTest() {
        return Stream.of(
                Arguments.of(null, null, "Attribute `number1` is invalid"),
                Arguments.of(null, ZERO, "Attribute `number1` is invalid"),
                Arguments.of(ZERO, null, "Attribute `number2` is invalid")
        );
    }

    private static Stream<Arguments> isFirstValueLesserTest() {
        return Stream.of(
                Arguments.of(ZERO, new BigDecimal("-1"), false),
                Arguments.of(ZERO, new BigDecimal("1"), true),
                Arguments.of(ZERO, ZERO, false)
        );
    }

    private static Stream<Arguments> isFirstValueLesserOrEqualExceptionTest() {
        return Stream.of(
                Arguments.of(null, null, "Attribute `number1` is invalid"),
                Arguments.of(null, ZERO, "Attribute `number1` is invalid"),
                Arguments.of(ZERO, null, "Attribute `number2` is invalid")
        );
    }

    private static Stream<Arguments> isFirstValueLesserOrEqualTest() {
        return Stream.of(
                Arguments.of(ZERO, new BigDecimal("-1"), false),
                Arguments.of(ZERO, new BigDecimal("1"), true),
                Arguments.of(ZERO, ZERO, true)
        );
    }

    private static Stream<Arguments> inRangeExceptionTest() {
        return Stream.of(
                Arguments.of(null, TupleRange.of(ZERO, ZERO), "Attribute `number` is invalid"),
                Arguments.of(ZERO, null, "Attribute `range` is invalid")
        );
    }

    private static Stream<Arguments> inRangeTest() {
        return Stream.of(
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("-2"), new BigDecimal("2")), true),
                Arguments.of(ZERO, TupleRange.of(ZERO, new BigDecimal("2")), false),
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("-2"), ZERO), false),
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("1"), new BigDecimal("2")), false),
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("-2"), new BigDecimal("-1")), false)
        );
    }

    private static Stream<Arguments> inRangeClosedExceptionTest() {
        return Stream.of(
                Arguments.of(null, TupleRange.of(ZERO, ZERO), "Attribute `number` is invalid"),
                Arguments.of(ZERO, null, "Attribute `range` is invalid")
        );
    }

    private static Stream<Arguments> inRangeClosedTest() {
        return Stream.of(
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("-2"), new BigDecimal("2")), true),
                Arguments.of(ZERO, TupleRange.of(ZERO, new BigDecimal("2")), true),
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("-2"), ZERO), true),
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("1"), new BigDecimal("2")), false),
                Arguments.of(ZERO, TupleRange.of(new BigDecimal("-2"), new BigDecimal("-1")), false)
        );
    }

    private static Stream<Arguments> isZeroTest() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(randomBigDecimalGreaterThanZero(), false),
                Arguments.of(ZERO, true),
                Arguments.of(randomBigDecimalLessThanZero(), false)
        );
    }

    private static Stream<Arguments> isNullOrZeroTest() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(randomBigDecimalGreaterThanZero(), false),
                Arguments.of(ZERO, true),
                Arguments.of(randomBigDecimalLessThanZero(), false)
        );
    }

    private static Stream<Arguments> isOneHundredTest() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(ZERO, false),
                Arguments.of(randomBigDecimalLessThanZero(), false),
                Arguments.of(ONE_HUNDRED, true),
                Arguments.of(new BigDecimal("100"), true),
                Arguments.of(new BigDecimal("100.00"), true),
                Arguments.of(new BigDecimal("100.00000"), true)
        );
    }

    private static Stream<Arguments> isPositiveTest() {
        return Stream.of(
                Arguments.of(randomBigDecimalGreaterThanZero(), true),
                Arguments.of(ZERO, false),
                Arguments.of(randomBigDecimalLessThanZero(), false)
        );
    }

    private static Stream<Arguments> isPositiveOrZeroTest() {
        return Stream.of(
                Arguments.of(randomBigDecimalGreaterThanZero(), true),
                Arguments.of(ZERO, true),
                Arguments.of(randomBigDecimalLessThanZero(), false)
        );
    }

    private static Stream<Arguments> isNegativeTest() {
        return Stream.of(
                Arguments.of(randomBigDecimalGreaterThanZero(), false),
                Arguments.of(ZERO, false),
                Arguments.of(randomBigDecimalLessThanZero(), true)
        );
    }

    private static Stream<Arguments> isNegativeOrZeroTest() {
        return Stream.of(
                Arguments.of(randomBigDecimalGreaterThanZero(), false),
                Arguments.of(ZERO, true),
                Arguments.of(randomBigDecimalLessThanZero(), true)
        );
    }

    @ParameterizedTest
    @MethodSource("areValuesEqualsTest")
    public void areValuesEqualsTest(BigDecimal value1, BigDecimal value2, boolean expected) {
        // Act
        var actual = areValuesEquals(value1, value2);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueGreaterExceptionTest")
    public void isFirstValueGreaterExceptionTest(BigDecimal value1, BigDecimal value2, String expected) {
        // Act
        var throwable = catchThrowable(() -> isFirstValueGreater(value1, value2));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueGreaterTest")
    public void isFirstValueGreaterTest(BigDecimal value1, BigDecimal value2, boolean expected) {
        // Act
        var actual = isFirstValueGreater(value1, value2);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueGreaterOrEqualExceptionTest")
    public void isFirstValueGreaterOrEqualExceptionTest(BigDecimal value1, BigDecimal value2, String expected) {
        // Act
        var throwable = catchThrowable(() -> isFirstValueGreaterOrEqual(value1, value2));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueGreaterOrEqualTest")
    public void isFirstValueGreaterOrEqualTest(BigDecimal value1, BigDecimal value2, boolean expected) {
        // Act
        var actual = isFirstValueGreaterOrEqual(value1, value2);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueLesserExceptionTest")
    public void isFirstValueLesserExceptionTest(BigDecimal value1, BigDecimal value2, String expected) {
        // Act
        var throwable = catchThrowable(() -> isFirstValueLesser(value1, value2));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueLesserTest")
    public void isFirstValueLesserTest(BigDecimal value1, BigDecimal value2, boolean expected) {
        // Act
        var actual = isFirstValueLesser(value1, value2);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueLesserOrEqualExceptionTest")
    public void isFirstValueLesserOrEqualExceptionTest(BigDecimal value1, BigDecimal value2, String expected) {
        // Act
        var throwable = catchThrowable(() -> isFirstValueLesserOrEqual(value1, value2));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFirstValueLesserOrEqualTest")
    public void isFirstValueLesserOrEqualTest(BigDecimal value1, BigDecimal value2, boolean expected) {
        // Act
        var actual = isFirstValueLesserOrEqual(value1, value2);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("inRangeExceptionTest")
    public void inRangeExceptionTest(BigDecimal value, TupleRange<BigDecimal> range, String expected) {
        // Act
        var throwable = catchThrowable(() -> inRange(value, range));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("inRangeTest")
    public void inRangeTest(BigDecimal value, TupleRange<BigDecimal> range, boolean expected) {
        // Act
        var actual = inRange(value, range);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("inRangeClosedExceptionTest")
    public void inRangeClosedExceptionTest(BigDecimal value, TupleRange<BigDecimal> range, String expected) {
        // Act
        var throwable = catchThrowable(() -> inRangeClosed(value, range));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("inRangeClosedTest")
    public void inRangeClosedTest(BigDecimal value, TupleRange<BigDecimal> range, boolean expected) {
        // Act
        var actual = inRangeClosed(value, range);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isZeroTest")
    public void isZeroTest(BigDecimal value, boolean expected) {
        // Act
        var actual = isZero(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isNullOrZeroTest")
    public void isNullOrZeroTest(BigDecimal value, boolean expected) {
        // Act
        var actual = isNullOrZero(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isOneHundredTest")
    public void isOneHundredTest(BigDecimal value, boolean expected) {
        // Act
        var actual = isOneHundred(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isPositiveTest")
    public void isPositiveTest(BigDecimal value, boolean expected) {
        // Act
        var actual = isPositive(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isPositiveOrZeroTest")
    public void isPositiveOrZeroTest(BigDecimal value, boolean expected) {
        // Act
        var actual = isPositiveOrZero(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isNegativeTest")
    public void isNegativeTest(BigDecimal value, boolean expected) {
        // Act
        var actual = isNegative(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isNegativeOrZeroTest")
    public void isNegativeOrZeroTest(BigDecimal value, boolean expected) {
        // Act
        var actual = isNegativeOrZero(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void absOrZeroTest() {
        // Arrange
        var positive = randomBigDecimalGreaterThanZero();
        var negative = randomBigDecimalLessThanZero();
        List<Tuple2<BigDecimal, BigDecimal>> cases = new ArrayList<>();
        cases.add(Tuple2.of(null, ZERO));
        cases.add(Tuple2.of(ZERO, ZERO));
        cases.add(Tuple2.of(positive, positive));
        cases.add(Tuple2.of(negative, negative.multiply(BigDecimal.valueOf(-1))));

        cases.forEach(item -> {
            // Arrange
            var value = item.getA();
            var expected = item.getB();

            // Act
            var actual = absOrZero(value);

            // Assert
            assertThat(actual).isEqualTo(expected);
        });
    }
}
