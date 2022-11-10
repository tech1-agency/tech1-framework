package io.tech1.framework.domain.utilities.random;

import io.tech1.framework.domain.constants.BigDecimalConstants;
import io.tech1.framework.domain.constants.BigIntegerConstants;
import io.tech1.framework.domain.tests.constants.TestsConstants;
import io.tech1.framework.domain.tests.enums.EnumOneValueUnderTests;
import io.tech1.framework.domain.tests.enums.EnumUnderTests;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.enums.EnumUnderTests.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static java.math.BigDecimal.ONE;
import static java.time.ZoneId.getAvailableZoneIds;
import static java.util.Arrays.asList;
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

    private static Stream<Arguments> containsPrimitiveWrapperTest() {
        return Stream.of(
                Arguments.of(Short.class, true),
                Arguments.of(Boolean.class, true),
                Arguments.of(Integer.class, true),
                Arguments.of(Long.class, true),
                Arguments.of(Double.class, true),
                Arguments.of(BigDecimal.class, true),
                Arguments.of(Float.class, false),
                Arguments.of(String.class, false)
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
        var lowerBound = randomIntegerGreaterThanZeroByBounds(50, 75);
        var upperBound = randomIntegerGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomIntegerGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual >= lowerBound).isTrue();
        assertThat(actual <= upperBound).isTrue();
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
        var lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        var upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomLongGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual >= lowerBound).isTrue();
        assertThat(actual <= upperBound).isTrue();
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
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigDecimalGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThanOrEqualTo(BigDecimal.valueOf(lowerBound));
        assertThat(actual).isLessThanOrEqualTo(BigDecimal.valueOf(upperBound));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigDecimalLessThanZeroByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigDecimalLessThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThanOrEqualTo(BigDecimal.valueOf(upperBound).multiply(BigDecimalConstants.MINUS_ONE));
        assertThat(actual).isLessThanOrEqualTo(BigDecimal.valueOf(lowerBound).multiply(BigDecimalConstants.MINUS_ONE));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigDecimalByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigDecimalByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThanOrEqualTo(BigDecimal.valueOf(upperBound).multiply(BigDecimalConstants.MINUS_ONE));
        assertThat(actual).isLessThanOrEqualTo(BigDecimal.valueOf(upperBound));
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
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigIntegerGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThanOrEqualTo(BigInteger.valueOf(lowerBound));
        assertThat(actual).isLessThanOrEqualTo(BigInteger.valueOf(upperBound));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigIntegerLessThanZeroByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigIntegerLessThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThanOrEqualTo(BigInteger.valueOf(upperBound).multiply(BigIntegerConstants.MINUS_ONE));
        assertThat(actual).isLessThanOrEqualTo(BigInteger.valueOf(lowerBound).multiply(BigIntegerConstants.MINUS_ONE));
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomBigIntegerByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigIntegerByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isGreaterThanOrEqualTo(BigInteger.valueOf(upperBound).multiply(BigIntegerConstants.MINUS_ONE));
        assertThat(actual).isLessThanOrEqualTo(BigInteger.valueOf(upperBound));
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
        var actual = randomStringLetterOrNumbersOnly(size);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.length()).isEqualTo(size);
        assertThat(Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(actual).find()).isFalse();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomIpAddressTest() {
        // Arrange
        var pattern = Pattern.compile("^"
                + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
                + "|"
                + "localhost" // localhost
                + "|"
                + "(([0-9]{1,3}\\.){3})[0-9]{1,3})" // IP
                + ":"
                + "[0-9]{1,5}$"); // Port

        // Act
        var actual = randomIpAddress();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(pattern.matcher(actual.split("://")[1]).matches()).isTrue();
    }

    @Test
    public void randomStringsAsListTest() {
        // Arrange
        var size = randomIntegerGreaterThanZeroByBounds(1, 5);
        var elementLength = 32;

        // Act
        var actual = randomStringsAsList(size);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(size);
        actual.forEach(element -> {
            assertThat(element).isNotNull();
            assertThat(element.length()).isEqualTo(elementLength);
        });
    }

    @Test
    public void randomStringsAsArrayTest() {
        // Arrange
        var size = randomIntegerGreaterThanZeroByBounds(1, 5);
        var elementLength = 32;

        // Act
        var actual = randomStringsAsArray(size);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.length).isEqualTo(size);
        asList(actual).forEach(element -> {
            assertThat(element).isNotNull();
            assertThat(element.length()).isEqualTo(elementLength);
        });
    }

    @Test
    public void randomEmailTest() {
        // Arrange
        var domain = "@tech1.io";
        var randomLength = 32;
        var domainLength = 9;
        var expected = randomLength + domainLength;

        // Act
        var actual = randomEmail();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.length()).isEqualTo(expected);
        assertThat(actual.substring(randomLength)).isEqualTo(domain);
    }

    @Test
    public void randomElementListTest() {
        // Arrange
        var list = List.of(
                EXAMPLE_1,
                EXAMPLE_2,
                EXAMPLE_3,
                EXAMPLE_4
        );

        // Act
        var actual = randomElement(list);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(list).contains(actual);
    }

    @Test
    public void randomElementSetTest() {
        // Arrange
        var set = Set.of(
                EXAMPLE_1,
                EXAMPLE_2,
                EXAMPLE_3,
                EXAMPLE_4
        );

        // Act
        var actual = randomElement(set);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(set).contains(actual);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomLocalDateTest() {
        // Act
        var actual = randomLocalDate();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getYear()).isGreaterThanOrEqualTo(2000);
        assertThat(actual.getYear()).isLessThanOrEqualTo(LocalDate.now().getYear());
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomLocalDateByBoundsTest() {
        // Arrange
        var minYear = randomIntegerGreaterThanZeroByBounds(2000, 2002);
        var maxYear = randomIntegerGreaterThanZeroByBounds(2020, 2022);

        // Act
        var actual = randomLocalDateByBounds(minYear, maxYear);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getYear()).isGreaterThanOrEqualTo(2000);
        assertThat(actual.getYear()).isLessThanOrEqualTo(2022);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomLocalDateTimeTest() {
        // Act
        var actual = randomLocalDateTime();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getYear()).isGreaterThanOrEqualTo(2000);
        assertThat(actual.getYear()).isLessThanOrEqualTo(LocalDate.now().getYear());
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomLocalDateTimeByBoundsTest() {
        // Arrange
        var minYear = randomIntegerGreaterThanZeroByBounds(2000, 2002);
        var maxYear = randomIntegerGreaterThanZeroByBounds(2020, 2022);

        // Act
        var actual = randomLocalDateTimeByBounds(minYear, maxYear);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getYear()).isGreaterThanOrEqualTo(2000);
        assertThat(actual.getYear()).isLessThanOrEqualTo(2022);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomDateTest() {
        // Act
        var actual = randomDate();

        // Assert
        assertThat(actual).isNotNull();

        var calendar = Calendar.getInstance();
        calendar.setTime(actual);
        assertThat(calendar.get(Calendar.YEAR)).isGreaterThanOrEqualTo(2000);
        assertThat(calendar.get(Calendar.YEAR)).isLessThanOrEqualTo(LocalDate.now().getYear());
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomDateByBoundsTest() {
        // Arrange
        var minYear = randomIntegerGreaterThanZeroByBounds(2000, 2002);
        var maxYear = randomIntegerGreaterThanZeroByBounds(2020, 2022);

        // Act
        var actual = randomDateByBounds(minYear, maxYear);

        // Assert
        assertThat(actual).isNotNull();

        var calendar = Calendar.getInstance();
        calendar.setTime(actual);
        assertThat(calendar.get(Calendar.YEAR)).isGreaterThanOrEqualTo(2000);
        assertThat(calendar.get(Calendar.YEAR)).isLessThanOrEqualTo(LocalDate.now().getYear());
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomEnumTest() {
        // Act
        var actual = randomEnum(EnumUnderTests.class);

        // Assert
        assertThat(EnumUnderTests.values()).contains(actual);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomEnumExceptTest() {
        // Arrange
        var enumValues = List.of(EnumUnderTests.values());
        var clazz = EnumUnderTests.class;
        var randomEnum = randomEnum(clazz);

        // Act
        var actual = randomEnumExcept(clazz, randomEnum);

        // Assert
        assertThat(enumValues).contains(randomEnum);
        assertThat(enumValues).contains(actual);
        assertThat(actual).isNotEqualTo(randomEnum);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomEnumExceptExceptionTest() {
        // Act
        var thrown = catchThrowable(() -> randomEnumExcept(EnumOneValueUnderTests.class, EnumOneValueUnderTests.ONE_VALUE));

        // Assert
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
        assertThat(thrown.getMessage()).isEqualTo("Please check enum: class io.tech1.framework.domain.tests.enums.EnumOneValueUnderTests");
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomEnumExceptCaseAsListTest() {
        // Arrange
        var enumValues = List.of(EnumUnderTests.values());
        var clazz = EnumUnderTests.class;
        var randomEnum1 = randomEnum(clazz);
        var randomEnum2 = randomEnumExcept(clazz, randomEnum1);
        var randomEnums = List.of(randomEnum1, randomEnum2);

        // Act
        var actual = randomEnumExcept(clazz, randomEnums);

        // Assert
        assertThat(enumValues).contains(randomEnum1);
        assertThat(enumValues).contains(randomEnum2);
        assertThat(enumValues).contains(actual);
        assertThat(actual).isNotEqualTo(randomEnum1);
        assertThat(actual).isNotEqualTo(randomEnum2);
        assertThat(randomEnum1).isNotEqualTo(randomEnum2);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomEnumExceptCaseAsListExceptionTest() {
        // Arrange
        var clazz = EnumUnderTests.class;
        var allPossibleEnumValues = List.of(EXAMPLE_1, EXAMPLE_2, EXAMPLE_3, EXAMPLE_4);

        // Act
        var thrown = catchThrowable(() -> randomEnumExcept(clazz, allPossibleEnumValues));

        // Assert
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
        assertThat(thrown.getMessage()).isEqualTo("Please check enum: class io.tech1.framework.domain.tests.enums.EnumUnderTests");
    }

    @ParameterizedTest
    @MethodSource("containsPrimitiveWrapperTest")
    public void containsPrimitiveWrapperTest(Class<?> primitiveWrapper, boolean expected) {
        // Act
        var actualContains = containsPrimitiveWrapper(primitiveWrapper);

        // Assert
        assertThat(actualContains).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("containsPrimitiveWrapperTest")
    public void randomListOfPrimitiveWrappersTest(Class<?> primitiveWrapper, boolean containsPrimitiveWrrapper) {
        // Arrange
        int size = randomIntegerGreaterThanZeroByBounds(2, 5);

        // Act
        var actual = randomListOfPrimitiveWrappers(primitiveWrapper, size);

        // Assert
        assertThat(actual).isNotNull();
        if (containsPrimitiveWrrapper) {
            actual.forEach(item -> {
                assertThat(item).isNotNull();
                assertThat(item.getClass()).isEqualTo(primitiveWrapper);
            });
            assertThat(actual).hasSize(size);
        } else {
            assertThat(actual).hasSize(0);
        }
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomZoneIdTest() {
        // Act
        var actual = randomZoneId();

        // Assert
        assertThat(getAvailableZoneIds()).contains(actual.getId());
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void randomTimeZoneTest() {
        // Act
        var actual = randomTimeZone();

        // Assert
        assertThat(getAvailableZoneIds()).contains(actual.toZoneId().getId());
    }
}
