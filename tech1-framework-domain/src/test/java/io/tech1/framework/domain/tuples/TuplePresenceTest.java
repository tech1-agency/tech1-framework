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

class TuplePresenceTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(TuplePresence.present("PRESENT"), "tuple-presence-present.json"),
                Arguments.of(TuplePresence.absent("ABSENT"), "tuple-presence-absent.json")
        );
    }

    @Override
    protected String getFolder() {
        return "tuples";
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    void serialize(TuplePresence<String> tuplePresence, String fileName) {
        // Act
        var json = this.writeValueAsString(tuplePresence);

        // Assert
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeTest")
    void deserializeTest(TuplePresence<String> tuplePresence, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<TuplePresence<String>>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isEqualTo(tuplePresence);
    }
}
