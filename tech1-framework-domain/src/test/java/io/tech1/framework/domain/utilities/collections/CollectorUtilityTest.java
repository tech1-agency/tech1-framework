package io.tech1.framework.domain.utilities.collections;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.tech1.framework.domain.utilities.collections.CollectorUtility.toSingleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@Slf4j
class CollectorUtilityTest {

    @Test
    void toSingletonExceptionTest() {
        // Arrange
        var list = List.of(1, 2, 3, 4);

        // Act
        var throwable = catchThrowable(() -> {
            var actual = list.stream()
                    .filter(element -> element % 2 == 0)
                    .collect(toSingleton());
            LOGGER.warn("Not reachable. Actual: `{}`", actual);
        });

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getMessage()).isEqualTo("Collector toSingleton() expects one element");
    }

    @Test
    void toSingletonTest() {
        // Arrange
        var list = List.of(1, 2, 3, 4);

        // Act
        var actual = list.stream()
                .filter(element -> element == 4)
                .collect(toSingleton());

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(4);
    }
}
