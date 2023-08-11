package io.tech1.framework.domain.utilities.random;

import feign.Request;
import io.tech1.framework.domain.constants.BigDecimalConstants;
import io.tech1.framework.domain.constants.BigIntegerConstants;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.exceptions.random.IllegalEnumException;
import io.tech1.framework.domain.tests.enums.EnumOneValueUnderTests;
import io.tech1.framework.domain.tests.enums.EnumUnderTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.RANDOM_ITERATIONS_COUNT;
import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static io.tech1.framework.domain.tests.enums.EnumUnderTests.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static java.math.BigDecimal.ONE;
import static java.time.ZoneId.getAvailableZoneIds;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@Slf4j
class RandomUtilityTest {

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
    void oneTest(Class<? extends Number> clazz, Number expected) {
        // Act
        var actual = one(clazz);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void oneTestException() {
        // Act
        var throwable = catchThrowable(() -> one(Float.class));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unexpected clazz: java.lang.Float");
    }

    @Test
    void randomShortTest() {
        // Act
        var actual = randomShort();

        // Assert
        assertThat(actual).isNotNull();
    }

    @Test
    void randomBooleanTest() {
        // Act
        var actual = randomBoolean();

        // Assert
        LOGGER.debug("randomBoolean is ignored: " + actual);
    }

    @Test
    void randomDoubleTest() {
        // Act
        var actual = randomDouble();

        // Assert
        assertThat(actual).isNotNull();
    }

    @Test
    void randomIntegerTest() {
        // Act
        var actual = randomInteger();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomIntegerGreaterThanZeroTest() {
        // Act
        var actual = randomIntegerGreaterThanZero();

        // Assert
        assertThat(actual).isPositive();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomIntegerLessThanZeroTest() {
        // Act
        var actual = randomIntegerLessThanZero();

        // Assert
        assertThat(actual).isNegative();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomIntegerGreaterThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = randomIntegerGreaterThanZeroByBounds(50, 75);
        var upperBound = randomIntegerGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomIntegerGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(lowerBound)
                .isLessThanOrEqualTo(upperBound);
    }

    @Test
    void randomLongTest() {
        // Act
        var actual = randomLong();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomLongGreaterThanZeroTest() {
        // Act
        var actual = randomLongGreaterThanZero();

        // Assert
        assertThat(actual).isPositive();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomLongLessThanZeroTest() {
        // Act
        var actual = randomLongLessThanZero();

        // Assert
        assertThat(actual).isNegative();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomLongGreaterThanZeroByBoundsTest() {
        // Arrange
        var lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        var upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomLongGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(lowerBound)
                .isLessThanOrEqualTo(upperBound);
    }

    @Test
    void randomBigDecimalTest() {
        // Act
        var actual = randomBigDecimal();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigDecimalGreaterThanZeroTest() {
        // Act
        var actual = randomBigDecimalGreaterThanZero();

        // Assert
        assertThat(actual).isGreaterThan(BigDecimal.ZERO);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigDecimalLessThanZeroTest() {
        // Act
        var actual = randomBigDecimalLessThanZero();

        // Assert
        assertThat(actual).isLessThan(BigDecimal.ZERO);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigDecimalGreaterThanZeroByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigDecimalGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(BigDecimal.valueOf(lowerBound))
                .isLessThanOrEqualTo(BigDecimal.valueOf(upperBound));
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigDecimalLessThanZeroByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigDecimalLessThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(BigDecimal.valueOf(upperBound).multiply(BigDecimalConstants.MINUS_ONE))
                .isLessThanOrEqualTo(BigDecimal.valueOf(lowerBound).multiply(BigDecimalConstants.MINUS_ONE));
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigDecimalByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigDecimalByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(BigDecimal.valueOf(upperBound).multiply(BigDecimalConstants.MINUS_ONE))
                .isLessThanOrEqualTo(BigDecimal.valueOf(upperBound));
    }

    @Test
    void randomBigIntegerTest() {
        // Act
        var actual = randomBigInteger();

        // Assert
        assertThat(actual).isNotNull();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigIntegerGreaterThanZeroTest() {
        // Act
        var actual = randomBigIntegerGreaterThanZero();

        // Assert
        assertThat(actual).isGreaterThan(BigInteger.ZERO);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigIntegerLessThanZeroTestt() {
        // Act
        var actual = randomBigIntegerLessThanZero();

        // Assert
        assertThat(actual).isLessThan(BigInteger.ZERO);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigIntegerGreaterThanZeroByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigIntegerGreaterThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(BigInteger.valueOf(lowerBound))
                .isLessThanOrEqualTo(BigInteger.valueOf(upperBound));
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigIntegerLessThanZeroByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigIntegerLessThanZeroByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(BigInteger.valueOf(upperBound).multiply(BigIntegerConstants.MINUS_ONE))
                .isLessThanOrEqualTo(BigInteger.valueOf(lowerBound).multiply(BigIntegerConstants.MINUS_ONE));
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomBigIntegerByBoundsTest() {
        // Arrange
        long lowerBound = randomLongGreaterThanZeroByBounds(50, 75);
        long upperBound = randomLongGreaterThanZeroByBounds(77, 100);

        // Act
        var actual = randomBigIntegerByBounds(lowerBound, upperBound);

        // Assert
        assertThat(actual)
                .isGreaterThanOrEqualTo(BigInteger.valueOf(upperBound).multiply(BigIntegerConstants.MINUS_ONE))
                .isLessThanOrEqualTo(BigInteger.valueOf(upperBound));
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomStringTest() {
        // Act
        var actual = randomString();

        // Assert
        assertThat(actual).hasSize(32);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomStringLetterOrNumbersOnlyTest() {
        // Arrange
        var regex = "[^a-z0-9 ]";
        var size = 40;

        // Act
        var actual = randomStringLetterOrNumbersOnly(size);

        // Assert
        assertThat(actual).hasSize(size);
        assertThat(Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(actual).find()).isFalse();
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomIPv4Test() {
        // Act
        var actual = randomIPv4();

        // Assert
        var ipv4 = List.of(actual.split("\\."));
        assertThat(ipv4).hasSize(4);
        ipv4.forEach(element -> {
            var slot = Integer.valueOf(element);
            assertThat(slot).isNotNull();
            assertThat(slot).isNotNegative();
            assertThat(slot).isLessThan(256);
        });
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomIPAddressTest() {
        // Act
        var actual = randomIPAddress();

        // Assert
        var ipv4 = List.of(actual.value().split("\\."));
        assertThat(ipv4).hasSize(4);
        ipv4.forEach(element -> {
            var slot = Integer.valueOf(element);
            assertThat(slot).isNotNull();
            assertThat(slot).isNotNegative();
            assertThat(slot).isLessThan(256);
        });
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void localhostTest() {
        // Act
        var actual = localhost();

        // Assert
        assertThat(actual.value()).isEqualTo("127.0.0.1");
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomServerURLTest() {
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
        var actual = randomServerURL();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(pattern.matcher(actual.split("://")[1]).matches()).isTrue();
    }

    @Test
    void randomStringsAsListTest() {
        // Arrange
        int size = randomIntegerGreaterThanZeroByBounds(1, 5);
        var elementLength = 32;

        // Act
        var actual = randomStringsAsList(size);

        // Assert
        assertThat(actual).hasSize(size);
        actual.forEach(element -> assertThat(element).hasSize(elementLength));
    }

    @Test
    void randomStringsAsSetTest() {
        // Arrange
        int size = randomIntegerGreaterThanZeroByBounds(1, 5);
        var elementLength = 32;

        // Act
        var actual = randomStringsAsSet(size);

        // Assert
        assertThat(actual.getClass()).isEqualTo(HashSet.class);
        assertThat(actual).hasSize(size);
        actual.forEach(element -> assertThat(element).hasSize(elementLength));
    }

    @Test
    void randomStringsAsArrayTest() {
        // Arrange
        int size = randomIntegerGreaterThanZeroByBounds(1, 5);
        var elementLength = 32;

        // Act
        var actual = randomStringsAsArray(size);

        // Assert
        assertThat(actual).hasSize(size);
        asList(actual).forEach(element -> assertThat(element).hasSize(elementLength));
    }

    @Test
    void randomEmailAsValueTest() {
        // Arrange
        var domain = "@tech1.io";
        var randomLength = 32;
        var domainLength = 9;
        var expected = randomLength + domainLength;

        // Act
        var actual = randomEmailAsValue();

        // Assert
        assertThat(actual).hasSize(expected);
        assertThat(actual.substring(randomLength)).isEqualTo(domain);
    }

    @Test
    void randomEmailTest() {
        // Arrange
        var domain = "@tech1.io";
        var randomLength = 32;
        var domainLength = 9;
        var expected = randomLength + domainLength;

        // Act
        var actual = randomEmail();

        // Assert
        assertThat(actual.value()).hasSize(expected);
        assertThat(actual.value().substring(randomLength)).isEqualTo(domain);
    }

    @Test
    void randomElementListTest() {
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
    void randomElementSetTest() {
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

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomLocalDateTest() {
        // Act
        var actual = randomLocalDate();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getYear()).isGreaterThanOrEqualTo(2000);
        assertThat(actual.getYear()).isLessThanOrEqualTo(LocalDate.now().getYear());
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomLocalDateByBoundsTest() {
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

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomLocalDateTimeTest() {
        // Act
        var actual = randomLocalDateTime();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getYear()).isGreaterThanOrEqualTo(2000);
        assertThat(actual.getYear()).isLessThanOrEqualTo(LocalDate.now().getYear());
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomLocalDateTimeByBoundsTest() {
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

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomDateTest() {
        // Act
        var actual = randomDate();

        // Assert
        assertThat(actual).isNotNull();

        var calendar = Calendar.getInstance();
        calendar.setTime(actual);
        assertThat(calendar.get(Calendar.YEAR)).isGreaterThanOrEqualTo(2000);
        assertThat(calendar.get(Calendar.YEAR)).isLessThanOrEqualTo(LocalDate.now().getYear());
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomDateByBoundsTest() {
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

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomEnumTest() {
        // Act
        var actual1 = randomEnum(EnumUnderTests.class);
        EnumUnderTests actual2 = randomEnumWildcard(EnumUnderTests.class);

        // Assert
        assertThat(EnumUnderTests.values()).contains(actual1);
        assertThat(EnumUnderTests.values()).contains(actual2);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomEnumExceptExceptionTest() {
        // Act
        var throwable1 = catchThrowable(() -> randomEnumExcept(EnumOneValueUnderTests.class, EnumOneValueUnderTests.ONE_VALUE));
        var throwable2 = catchThrowable(() -> randomEnumExceptWildcard(EnumOneValueUnderTests.class, EnumOneValueUnderTests.ONE_VALUE));

        // Assert
        assertThat(throwable1).isInstanceOf(IllegalEnumException.class);
        assertThat(throwable2).isInstanceOf(IllegalEnumException.class);
        var message = "Please check enum: class io.tech1.framework.domain.tests.enums.EnumOneValueUnderTests";
        assertThat(throwable1.getMessage()).isEqualTo(message);
        assertThat(throwable2.getMessage()).isEqualTo(message);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomEnumExceptWildcardTest() {
        // Arrange
        var enumValues = List.of(EnumUnderTests.values());
        var clazz = EnumUnderTests.class;
        var randomEnum = randomEnum(clazz);

        // Act
        var actual1 = randomEnumExcept(clazz, randomEnum);
        var actual2 = randomEnumExceptWildcard(clazz, randomEnum);

        // Assert
        assertThat(enumValues)
                .contains(randomEnum)
                .contains(actual2)
                .contains(actual1);
        assertThat(actual2).isNotEqualTo(randomEnum);
        assertThat(actual1).isNotEqualTo(randomEnum);
    }

    @RepeatedTest(RANDOM_ITERATIONS_COUNT)
    void randomEnumExceptCaseAsListTest() {
        // Arrange
        var enumValues = List.of(EnumUnderTests.values());
        var clazz = EnumUnderTests.class;
        var randomEnum1 = randomEnum(clazz);
        var randomEnum2 = randomEnumExcept(clazz, randomEnum1);
        var randomEnums = List.of(randomEnum1, randomEnum2);

        // Act
        var actual1 = randomEnumExcept(clazz, randomEnums);
        var actual2 = randomEnumExceptWildcard(clazz, randomEnums);

        // Assert
        assertThat(enumValues)
                .contains(randomEnum1)
                .contains(randomEnum2)
                .contains(actual1)
                .contains(actual2);
        assertThat(actual1).isNotEqualTo(randomEnum1);
        assertThat(actual2).isNotEqualTo(randomEnum1);
        assertThat(actual1).isNotEqualTo(randomEnum2);
        assertThat(actual2).isNotEqualTo(randomEnum2);
        assertThat(randomEnum1).isNotEqualTo(randomEnum2);
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomEnumExceptCaseAsListExceptionTest() {
        // Arrange
        var clazz = EnumUnderTests.class;
        var allPossibleEnumValues = List.of(EXAMPLE_1, EXAMPLE_2, EXAMPLE_3, EXAMPLE_4);

        // Act
        var throwable1 = catchThrowable(() -> randomEnumExcept(clazz, allPossibleEnumValues));
        var throwable2 = catchThrowable(() -> randomEnumExceptWildcard(clazz, allPossibleEnumValues));

        // Assert
        assertThat(throwable1).isInstanceOf(IllegalEnumException.class);
        assertThat(throwable2).isInstanceOf(IllegalEnumException.class);
        var message = "Please check enum: class io.tech1.framework.domain.tests.enums.EnumUnderTests";
        assertThat(throwable1.getMessage()).isEqualTo(message);
        assertThat(throwable2.getMessage()).isEqualTo(message);
    }

    @ParameterizedTest
    @MethodSource("containsPrimitiveWrapperTest")
    void containsPrimitiveWrapperTest(Class<?> primitiveWrapper, boolean expected) {
        // Act
        var actualContains = containsPrimitiveWrapper(primitiveWrapper);

        // Assert
        assertThat(actualContains).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("containsPrimitiveWrapperTest")
    void randomListOfPrimitiveWrappersTest(Class<?> primitiveWrapper, boolean containsPrimitiveWrrapper) {
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
            assertThat(actual).isEmpty();
        }
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomMethodTest() {
        // Act
        var actual = randomMethod();

        // Assert
        assertThat(actual.getName()).isEqualTo("finalize");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomZoneIdTest() {
        // Act
        var actual = randomZoneId();

        // Assert
        assertThat(getAvailableZoneIds()).contains(actual.getId());
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomTimeZoneTest() {
        // Act
        var actual = randomTimeZone();

        // Assert
        assertThat(getAvailableZoneIds()).contains(actual.toZoneId().getId());
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomUsernameTest() {
        // Act
        var actual = randomUsername();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.identifier()).isNotNull();
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomPasswordTest() {
        // Act
        var actual = randomPassword();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isNotNull();
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomFeignExceptionTest() {
        // Act
        var actual = randomFeignException();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.request().httpMethod()).isEqualTo(Request.HttpMethod.GET);
        assertThat(actual.request().url()).isEqualTo("/endpoint");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void validGeoLocationTest() {
        // Act
        var actual = validGeoLocation();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getIpAddr()).isNotNull();
        assertThat(actual.getCountry()).isEqualTo("Ukraine");
        assertThat(actual.getCountryCode()).isEqualTo("UA");
        assertThat(actual.getCountryFlag()).isEqualTo("🇺🇦");
        assertThat(actual.getCity()).isEqualTo("Lviv");
        assertThat(actual.getExceptionDetails()).isEmpty();
        assertThat(actual.getWhere()).isEqualTo("Ukraine, Lviv");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void invalidGeoLocationTest() {
        // Act
        var actual = invalidGeoLocation();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getIpAddr()).isNotNull();
        assertThat(actual.getCountry()).isEqualTo(UNKNOWN);
        assertThat(actual.getCity()).isEqualTo(UNKNOWN);
        assertThat(actual.getExceptionDetails()).isEqualTo("Location is unknown");
        assertThat(actual.getWhere()).isEqualTo("Unknown, Unknown");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomGeoLocationTest() {
        // Act
        var actual = randomGeoLocation();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getIpAddr()).isNotNull();
        assertThat(actual.getCountry()).isNotNull();
        assertThat(actual.getExceptionDetails()).isNotNull();
        assertThat(actual.getWhere()).isNotNull();
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void validUserAgentDetailsTest() {
        // Act
        var actual = validUserAgentDetails();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getBrowser()).isEqualTo("Chrome");
        assertThat(actual.getPlatform()).isEqualTo("macOS");
        assertThat(actual.getDeviceType()).isEqualTo("Desktop");
        assertThat(actual.getExceptionDetails()).isEmpty();
        assertThat(actual.getWhat()).isEqualTo("Chrome, macOS on Desktop");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void invalidUserAgentDetailsTest() {
        // Act
        var actual = invalidUserAgentDetails();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getBrowser()).isEqualTo(UNKNOWN);
        assertThat(actual.getPlatform()).isEqualTo(UNKNOWN);
        assertThat(actual.getDeviceType()).isEqualTo(UNKNOWN);
        assertThat(actual.getExceptionDetails()).isEqualTo("User agent details are unknown");
        assertThat(actual.getWhat()).isEqualTo("Unknown, Unknown on Unknown");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomUserAgentDetailsTest() {
        // Act
        var userAgentDetails = randomUserAgentDetails();

        // Assert
        assertThat(userAgentDetails).isNotNull();
        assertThat(userAgentDetails.getBrowser()).isNotNull();
        assertThat(userAgentDetails.getPlatform()).isNotNull();
        assertThat(userAgentDetails.getDeviceType()).isNotNull();
        assertThat(userAgentDetails.getExceptionDetails()).isNotNull();
        assertThat(userAgentDetails.getWhat()).isNotNull();
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void validUserRequestMetadataTest() {
        // Act
        var actual = validUserRequestMetadata();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(actual.getGeoLocation().getIpAddr()).isNotNull();
        assertThat(actual.getGeoLocation().getCountry()).isEqualTo("Ukraine");
        assertThat(actual.getGeoLocation().getCity()).isEqualTo("Lviv");
        assertThat(actual.getGeoLocation().getExceptionDetails()).isEmpty();
        assertThat(actual.getGeoLocation().getWhere()).isEqualTo("Ukraine, Lviv");
        assertThat(actual.getUserAgentDetails().getBrowser()).isEqualTo("Chrome");
        assertThat(actual.getUserAgentDetails().getPlatform()).isEqualTo("macOS");
        assertThat(actual.getUserAgentDetails().getDeviceType()).isEqualTo("Desktop");
        assertThat(actual.getUserAgentDetails().getExceptionDetails()).isEmpty();
        assertThat(actual.getUserAgentDetails().getWhat()).isEqualTo("Chrome, macOS on Desktop");
        assertThat(actual.getWhatTuple2().a()).isEqualTo("Chrome");
        assertThat(actual.getWhatTuple2().b()).isEqualTo("Chrome, macOS on Desktop");
        assertThat(actual.getWhereTuple3().a()).isNotNull();
        assertThat(actual.getWhereTuple3().a().split("\\.")).hasSize(4);
        assertThat(actual.getWhereTuple3().b()).isEqualTo("🇺🇦");
        assertThat(actual.getWhereTuple3().c()).isEqualTo("Ukraine, Lviv");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void invalidUserRequestMetadataTest() {
        // Act
        var actual = invalidUserRequestMetadata();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(actual.getGeoLocation().getIpAddr()).isNotNull();
        assertThat(actual.getGeoLocation().getCountry()).isEqualTo("Unknown");
        assertThat(actual.getGeoLocation().getCity()).isEqualTo("Unknown");
        assertThat(actual.getGeoLocation().getExceptionDetails()).isEqualTo("Location is unknown");
        assertThat(actual.getGeoLocation().getWhere()).isEqualTo("Unknown, Unknown");
        assertThat(actual.getUserAgentDetails().getBrowser()).isEqualTo("Unknown");
        assertThat(actual.getUserAgentDetails().getPlatform()).isEqualTo("Unknown");
        assertThat(actual.getUserAgentDetails().getDeviceType()).isEqualTo("Unknown");
        assertThat(actual.getUserAgentDetails().getExceptionDetails()).isEqualTo("User agent details are unknown");
        assertThat(actual.getUserAgentDetails().getWhat()).isEqualTo("Unknown, Unknown on Unknown");
        assertThat(actual.getWhatTuple2().a()).isEqualTo("Unknown");
        assertThat(actual.getWhatTuple2().b()).isEqualTo("Unknown, Unknown on Unknown");
        assertThat(actual.getWhereTuple3().a()).isNotNull();
        assertThat(actual.getWhereTuple3().a().split("\\.")).hasSize(4);
        assertThat(actual.getWhereTuple3().b()).isEqualTo("🏴‍");
        assertThat(actual.getWhereTuple3().c()).isEqualTo("Unknown, Unknown");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomUserRequestMetadataTest() {
        // Act
        var actual = randomUserRequestMetadata();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isNotNull();
        assertThat(actual.getGeoLocation().getIpAddr()).isNotNull();
        assertThat(actual.getGeoLocation().getCountry()).isNotNull();
        assertThat(actual.getGeoLocation().getCity()).isNotNull();
        assertThat(actual.getGeoLocation().getExceptionDetails()).isNotNull();
        assertThat(actual.getGeoLocation().getWhere()).isNotNull();
        assertThat(actual.getUserAgentDetails().getBrowser()).isNotNull();
        assertThat(actual.getUserAgentDetails().getPlatform()).isNotNull();
        assertThat(actual.getUserAgentDetails().getDeviceType()).isNotNull();
        assertThat(actual.getUserAgentDetails().getExceptionDetails()).isNotNull();
        assertThat(actual.getUserAgentDetails().getWhat()).isNotNull();
        assertThat(actual.getWhatTuple2().a()).isNotNull();
        assertThat(actual.getWhatTuple2().b()).isNotNull();
        assertThat(actual.getWhereTuple3().a()).isNotNull();
        assertThat(actual.getWhereTuple3().b()).isNotNull();
        assertThat(actual.getWhereTuple3().c()).isNotNull();
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomHardwareMonitoringThresholdTest() {
        // Act
        var actual = randomHardwareMonitoringThreshold();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isGreaterThanOrEqualTo(new BigDecimal("50"));
        assertThat(actual.value()).isLessThanOrEqualTo(new BigDecimal("100"));
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomHardwareMonitoringThresholdsTest() {
        // Act
        var actual = randomHardwareMonitoringThresholds();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getThresholds()).hasSize(5);
        actual.getThresholds().values().forEach(threshold -> {
            assertThat(threshold.value()).isGreaterThanOrEqualTo(new BigDecimal("50"));
            assertThat(threshold.value()).isLessThanOrEqualTo(new BigDecimal("100"));
        });
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomHardwareMonitoringDatapointTableRowTest() {
        // Act
        var actual = randomHardwareMonitoringDatapointTableRow();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.isThresholdReached()).isFalse();
    }
}
