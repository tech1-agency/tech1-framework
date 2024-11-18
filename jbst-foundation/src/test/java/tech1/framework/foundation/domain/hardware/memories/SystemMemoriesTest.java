package tech1.framework.foundation.domain.hardware.memories;

import com.fasterxml.jackson.core.type.TypeReference;
import tech1.framework.foundation.domain.tests.io.TestsIOUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SystemMemoriesTest extends AbstractMemoriesTest {

    private static Stream<Arguments> serializeDeserializeTest() {
        return Stream.of(
                Arguments.of(new SystemMemories(GlobalMemory.zeroUsage(), CpuMemory.zeroUsage()), "system-memory-1.json")
        );
    }

    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    void serializeTest(SystemMemories systemMemories, String fileName) {
        // Act
        var json = this.writeValueAsString(systemMemories);

        // Assert
        assertThat(json).isEqualTo(TestsIOUtils.readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    void deserializeTest(SystemMemories systemMemories, String fileName) {
        // Arrange
        var json = TestsIOUtils.readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<SystemMemories>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(systemMemories);
    }
}
