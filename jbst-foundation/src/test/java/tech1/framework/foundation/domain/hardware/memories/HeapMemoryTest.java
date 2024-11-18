package tech1.framework.foundation.domain.hardware.memories;

import com.fasterxml.jackson.core.type.TypeReference;
import tech1.framework.foundation.domain.tests.io.TestsIOUtils;
import tech1.framework.foundation.domain.tests.runners.AbstractObjectMapperRunner;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class HeapMemoryTest extends AbstractMemoriesTest {

    private static Stream<Arguments> serializeDeserializeTest() {
        return Stream.of(
                Arguments.of(new HeapMemory(
                        1073741824L,
                        573741824L,
                        1073741824L,
                        1073741824L
                ), "heap-memory-1.json"),
                Arguments.of(HeapMemory.zeroUsage(), "heap-memory-2.json")
        );
    }

    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    void serializeTest(HeapMemory heapMemory, String fileName) {
        // Act
        var json = this.writeValueAsString(heapMemory);

        // Assert
        Assertions.assertThat(json).isEqualTo(TestsIOUtils.readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    void deserializeTest(HeapMemory heapMemory, String fileName) {
        // Arrange
        var json = TestsIOUtils.readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<HeapMemory>() {};

        // Act
        var actual = AbstractObjectMapperRunner.OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(heapMemory);
    }
}
