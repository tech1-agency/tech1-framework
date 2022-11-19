package io.tech1.framework.domain.hardware.memories;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

public class SystemMemoriesTest extends AbstractMemoriesTest {

    private static Stream<Arguments> serializeDeserializeTest() {
        return Stream.of(
                Arguments.of(new SystemMemories(GlobalMemory.zeroUsage(), CpuMemory.zeroUsage()), "system-memory-1.json")
        );
    }

    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    public void serializeTest(SystemMemories systemMemories, String fileName) {
        // Act
        var json = this.writeValueAsString(systemMemories);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    public void deserializeTest(SystemMemories systemMemories, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<SystemMemories>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(systemMemories);
    }
}
