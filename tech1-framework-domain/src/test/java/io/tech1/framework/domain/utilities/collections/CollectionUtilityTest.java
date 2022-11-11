package io.tech1.framework.domain.utilities.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static io.tech1.framework.domain.utilities.collections.CollectionUtility.emptyQueue;
import static io.tech1.framework.domain.utilities.collections.CollectionUtility.union;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectionUtilityTest {

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
}
