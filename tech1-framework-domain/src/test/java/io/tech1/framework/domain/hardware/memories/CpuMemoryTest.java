package io.tech1.framework.domain.hardware.memories;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

public class CpuMemoryTest extends AbstractMemoriesTest {

    private static Stream<Arguments> serializeDeserializeTest() {
        return Stream.of(
                Arguments.of(new CpuMemory(new BigDecimal("1.234")), "cpu-memory-1.json"),
                Arguments.of(new CpuMemory(new BigDecimal("1.23456")), "cpu-memory-1.json"),
                Arguments.of(new CpuMemory(new BigDecimal("1.2")), "cpu-memory-2.json"),
                Arguments.of(CpuMemory.zeroUsage(), "cpu-memory-3.json")
        );
    }

    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    public void serializeTest(CpuMemory cpuMemory, String fileName) {
        // Act
        var json = this.writeValueAsString(cpuMemory);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeDeserializeTest")
    public void deserializeTest(CpuMemory cpuMemory, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<CpuMemory>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(cpuMemory);
    }
}
