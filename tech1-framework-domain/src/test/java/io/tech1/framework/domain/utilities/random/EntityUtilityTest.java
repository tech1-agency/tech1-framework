package io.tech1.framework.domain.utilities.random;

import io.tech1.framework.domain.tests.classes.*;
import io.tech1.framework.domain.tests.constants.TestsConstants;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.EntityUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.one;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class EntityUtilityTest {

    @Test
    public void listDefaultConstructorTest() {
        // Arrange
        var size = (int) one(Integer.class);

        // Act
        var list = list(ClassDefaultConstructor.class, size);

        // Assert
        assertThat(list).hasSize(size);
    }

    @Test
    public void listNoDefaultConstructorTest() {
        // Arrange
        var size = (int) one(Integer.class);

        // Act
        var list = list(ClassNoDefaultConstructor.class, size);

        // Assert
        assertThat(list).hasSize(size);
    }

    @Test
    public void listPrivateConstructorTest() {
        // Arrange
        var size = (int) one(Integer.class);

        // Act
        var throwable = AssertionsForClassTypes.catchThrowable(() -> list(ClassPrivateConstructor.class, size));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Please add entity construction rules or extend functionality. Class: `io.tech1.framework.domain.tests.classes.ClassPrivateConstructor`");
    }

    @Test
    public void setDefaultConstructorTest() {
        // Arrange
        var size = (int) one(Integer.class);

        // Act
        var set = set(ClassDefaultConstructor.class, size);

        // Assert
        assertThat(set).hasSize(size);
    }

    @Test
    public void setNoDefaultConstructorTest() {
        // Arrange
        var size = (int) one(Integer.class);

        // Act
        var set = set(ClassNoDefaultConstructor.class, size);

        // Assert
        assertThat(set).hasSize(size);
    }

    @Test
    public void setPrivateConstructorTest() {
        // Arrange
        var size = (int) one(Integer.class);

        // Act
        var throwable = catchThrowable(() -> set(ClassPrivateConstructor.class, size));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Please add entity construction rules or extend functionality. Class: `io.tech1.framework.domain.tests.classes.ClassPrivateConstructor`");
    }

    // =================================================================================================================
    // PROTECTED CLASSES
    // =================================================================================================================
    @SuppressWarnings("ConstantConditions")
    @Test
    public void processSettersInstanceNullTest() {
        // Act
        var throwable = catchThrowable(() -> setObjectFields(null));

        // Assert
        assertThat(throwable).isInstanceOf(ReflectiveOperationException.class);
        assertThat(throwable).hasMessageContaining("Cannot invoke setter. Instance is null");
    }

    @Test
    public void processSettersClassDefaultConstructorNoSettersTest() throws ReflectiveOperationException {
        // Arrange
        var instance = new ClassDefaultConstructorNoSetters();

        // Act
        setObjectFields(instance);

        // Assert
        assertThat(instance.getString()).isNull();
    }

    @Test
    public void processSettersClassDefaultConstructorUnexpectedMethodTest() throws ReflectiveOperationException {
        // Arrange
        var instance = new ClassDefaultConstructorUnexpectedMethods();

        // Act
        setObjectFields(instance);

        // Assert
        assertThat(instance.getString()).isNull();
    }

    @Test
    public void processSettersUnexpectedSetterLoggerCaseTest() {
        // Arrange
        var instance = new ClassDefaultConstructorUnexpectedSetter();

        // Act
        var throwable
                = catchThrowable(() -> setObjectFields(instance));

        // Assert
        assertThat(throwable).isInstanceOf(ReflectiveOperationException.class);
        assertThat(throwable).hasMessageContaining("Cannot invoke setter. Unexpected setter signature");
    }

    @Test
    public void processSettersClassWithAllArgsAndDefaultConstructorsTest() throws ReflectiveOperationException {
        // Arrange
        var instance = new ClassWithAllArgsAndDefaultConstructors();

        // Act
        setObjectFields(instance);

        // Assert
        assertThat(instance.getStringValue()).isNotNull();
    }

    @Test
    public void randomNestedClassesTest() {
        // Arrange
        var random = entity(ClassNestParent.class);

        // Assert
        assertThat(random).isNotNull();
        assertThat(random.getValue1()).isNotNull();
        assertThat(random.getValue2()).isNotNull();
        assertThat(random.getValue3()).isNotNull();
        assertThat(random.getValue4()).isNotNull();
        assertThat(random.getChild1()).isNotNull();
        assertThat(random.getChild1().getNest1Value1()).isNotNull();
        assertThat(random.getChild1().getNest1Value2()).isNotNull();
        assertThat(random.getChild1().getNest1Value3()).isNotNull();
        assertThat(random.getChild2()).isNotNull();
        assertThat(random.getChild2().getNest2Value1()).isNotNull();
        assertThat(random.getChild2().getNest2Value2()).isNotNull();
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void list345Test() {
        // Act
        var set = list345(Long.class);

        // Assert
        assertThat(set).isNotNull();
        assertThat(set.size()).isBetween(1, 6);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void set345Test() {
        // Act
        var set = set345(Long.class);

        // Assert
        assertThat(set).isNotNull();
        assertThat(set.size()).isBetween(1, 6);
    }
}
