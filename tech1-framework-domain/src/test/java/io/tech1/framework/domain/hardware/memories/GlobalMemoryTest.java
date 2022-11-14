package io.tech1.framework.domain.hardware.memories;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

public class GlobalMemoryTest extends AbstractMemoriesTest {

    private static Stream<Arguments> serializeDeserializeTest() {
        return Stream.of(
                Arguments.of(new GlobalMemory(
                        1073741824L,
                        1973741824L,
                        1073741824L,
                        1773741824L,
                        1073741824L,
                        1673741824L
                ), "global-memories-1.json"),
                Arguments.of(GlobalMemory.zeroUsage(), "global-memories-2.json")
        );
    }

    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    public void serializeTest(GlobalMemory globalMemory, String fileName) {
        // Act
        var json = this.writeValueAsString(globalMemory);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    public void deserializeTest(GlobalMemory globalMemory, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<GlobalMemory>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(globalMemory);
    }
}
