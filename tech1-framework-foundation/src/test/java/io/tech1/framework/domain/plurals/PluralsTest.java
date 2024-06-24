package io.tech1.framework.domain.plurals;

import io.tech1.framework.domain.base.ObjectId;
import io.tech1.framework.domain.tests.classes.TestObject;
import io.tech1.framework.domain.tests.classes.TestObjects;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class PluralsTest {

    @Test
    void randomTest() {
        // Arrange
        int size = 3;

        // Act
        var testObjects = TestObjects.random(size);

        // Assert
        assertThat(testObjects.getValues()).hasSize(size);
        assertThat(testObjects.getMappedValues()).hasSize(size);
        assertThat(testObjects.getNames()).hasSize(size);
    }

    @Test
    void unmodifiableValuesTest() {
        // Arrange
        int size = 3;
        var testObjects = TestObjects.random(size);

        // Act
        var throwable = catchThrowable(() -> testObjects.getValues().add(TestObject.random()));

        // Arrange
        assertThat(throwable).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableMappedValuesTest() {
        // Arrange
        int size = 3;
        var testObjects = TestObjects.random(size);

        // Act
        var throwable = catchThrowable(() -> testObjects.getMappedValues().put(ObjectId.random(), TestObject.random()));

        // Arrange
        assertThat(throwable).isInstanceOf(UnsupportedOperationException.class);
    }
}
