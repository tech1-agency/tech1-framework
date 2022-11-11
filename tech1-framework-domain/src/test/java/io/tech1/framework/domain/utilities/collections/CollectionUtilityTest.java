package io.tech1.framework.domain.utilities.collections;

import io.tech1.framework.domain.tests.constants.TestsConstants;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.collections.CollectionUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CollectionUtilityTest {

    private static Stream<Arguments> getFirstNElementsTest() {
        return Stream.of(
                Arguments.of(List.of(0, 1, 2, 3, 4), 2, List.of(0, 1)),
                Arguments.of(List.of(0, 1, 2, 3, 4), 10, List.of(0, 1, 2, 3, 4)),
                Arguments.of(List.of(0, 1, 2, 3, 4), 5, List.of(0, 1, 2, 3, 4)),
                Arguments.of(List.of(), 5, List.of()),
                Arguments.of(List.of(0, 1, 2, 3, 4), 0, List.of())
        );
    }

    private static Stream<Arguments> getLastNElementsTest() {
        return Stream.of(
                Arguments.of(List.of(0, 1, 2, 3, 4), 2, List.of(3, 4)),
                Arguments.of(List.of(0, 1, 2, 3, 4), 10, List.of(0, 1, 2, 3, 4)),
                Arguments.of(List.of(0, 1, 2, 3, 4), 5, List.of(0, 1, 2, 3, 4)),
                Arguments.of(List.of(), 5, List.of()),
                Arguments.of(List.of(0, 1, 2, 3, 4), 0, List.of())
        );
    }

    private static Stream<Arguments> maxOrZeroTest() {
        return Stream.of(
                Arguments.of(null, BigDecimal.ZERO),
                Arguments.of(List.of(), BigDecimal.ZERO),
                Arguments.of(List.of(new BigDecimal(0)), BigDecimal.ZERO),
                Arguments.of(List.of(new BigDecimal(-100), new BigDecimal(-1), new BigDecimal(-5)), new BigDecimal(-1)),
                Arguments.of(List.of(new BigDecimal(4), new BigDecimal(9), new BigDecimal(18)), new BigDecimal(18))
        );
    }

    private static Stream<Arguments> minOrZeroTest() {
        return Stream.of(
                Arguments.of(null, BigDecimal.ZERO),
                Arguments.of(List.of(), BigDecimal.ZERO),
                Arguments.of(List.of(new BigDecimal(0)), BigDecimal.ZERO),
                Arguments.of(List.of(new BigDecimal(-100), new BigDecimal(-1), new BigDecimal(-5)), new BigDecimal(-100)),
                Arguments.of(List.of(new BigDecimal(4), new BigDecimal(9), new BigDecimal(18)), new BigDecimal(4))
        );
    }

    @Test
    public void emptyQueueTest() {
        // Arrange
        var expected = new LinkedList<>();

        // Act
        var actual = emptyQueue();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void unionListsTest() {
        // Arrange
        var list1 = randomStringsAsList(randomIntegerGreaterThanZeroByBounds(3, 10));
        var list2 = randomStringsAsList(randomIntegerGreaterThanZeroByBounds(3, 10));

        // Act
        var actual = union(list1, list2);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getClass()).isEqualTo(ArrayList.class);
        assertThat(actual).hasSize(list1.size() + list2.size());
        assertThat(actual).containsAll(list1);
        assertThat(actual).containsAll(list2);
    }

    @Test
    public void unionSetTest() {
        // Arrange
        var set1 = randomStringsAsSet(randomIntegerGreaterThanZeroByBounds(3, 10));
        var set2 = randomStringsAsSet(randomIntegerGreaterThanZeroByBounds(3, 10));

        // Act
        var actual = union(set1, set2);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getClass()).isEqualTo(HashSet.class);
        assertThat(actual).hasSize(set1.size() + set2.size());
        assertThat(actual).containsAll(set1);
        assertThat(actual).containsAll(set2);
    }

    @Test
    public void safeGettersExceptionCases() {
        // Arrange
        var emptyList = List.of();
        var emptySet = Set.of();

        var throwable1 = catchThrowable(() -> safeFirstElement(emptyList));
        var throwable2 = catchThrowable(() -> safeFirstElement(emptySet));
        var throwable3 = catchThrowable(() -> safeLastElement(emptyList));
        var throwable4 = catchThrowable(() -> safeLastElement(emptyList));

        // Assert
        List.of(throwable1, throwable2, throwable3, throwable4).forEach(throwable -> {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(NullPointerException.class);
        });
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void safeGetters() {
        // Arrange
        var list = List.of(1, 2, 3, 4, 5);
        var set = new LinkedHashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);

        var first1 = safeFirstElement(list);
        var first2 = safeFirstElement(set);
        var last1 = safeLastElement(list);
        var last2 = safeLastElement(set);

        // Assert
        List.of(first1, first2).forEach(first -> {
            assertThat(first).isNotNull();
            assertThat(first).isEqualTo(1);
        });
        List.of(last1, last2).forEach(last -> {
            assertThat(last).isNotNull();
            assertThat(last).isEqualTo(5);
        });
    }

    @ParameterizedTest
    @MethodSource("getFirstNElementsTest")
    public void getFirstNElementsTest(List<Integer> list, int size, List<Integer> expected) {
        // Act
        var actual = getFirstNElements(list, size);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getFirstNElementsExceptionTest() {
        // Arrange
        var list = List.of(0, 1, 2, 3, 4);

        // Act
        var throwable = catchThrowable(() -> getFirstNElements(list, randomIntegerLessThanZero()));

        // Assert
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Elements quantity can't be negative");
    }

    @ParameterizedTest
    @MethodSource("getLastNElementsTest")
    public void getLastNElementsTest(List<Integer> list, int size, List<Integer> expected) {
        // Act
        var actual = getLastNElements(list, size);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getLastNElementsExceptionTest() {
        // Arrange
        var list = List.of(0, 1, 2, 3, 4);

        // Act
        var throwable = catchThrowable(() -> getLastNElements(list, randomIntegerLessThanZero()));

        // Assert
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Elements quantity can't be negative");
    }

    @ParameterizedTest
    @MethodSource("maxOrZeroTest")
    public void maxOrZeroTest(List<BigDecimal> list, BigDecimal expected) {
        // Act
        var actual = maxOrZero(list);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("minOrZeroTest")
    public void minOrZeroTest(List<BigDecimal> list, BigDecimal expected) {
        // Act
        var actual = minOrZero(list);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
