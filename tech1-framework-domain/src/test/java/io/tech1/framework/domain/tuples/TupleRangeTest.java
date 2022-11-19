package io.tech1.framework.domain.tuples;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

public class TupleRangeTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(TupleRange.of(100, 200), "tuple-range-integer.json"),
                Arguments.of(TupleRange.of("-1", "1"), "tuple-range-string.json"),
                Arguments.of(TupleRange.of(1.23d, 100.0d), "tuple-range-double.json")
        );
    }

    @Override
    protected String getFolder() {
        return "tuples";
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    public void serialize(TupleRange<?> tupleRange, String fileName) {
        // Act
        var json = this.writeValueAsString(tupleRange);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeTest")
    public void deserializeTest(TupleRange<?> tupleRange, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<TupleRange<?>>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(tupleRange);
    }
}
