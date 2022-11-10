package io.tech1.framework.domain.tuples;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.io.TestsIOUtils;
import io.tech1.framework.domain.tests.runners.AbstractObjectMapperRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TupleExceptionDetailsTest extends AbstractObjectMapperRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(TupleExceptionDetails.ok(), "tuple-exception-details-ok.json"),
                Arguments.of(TupleExceptionDetails.exception("tech1"), "tuple-exception-detials-exception.json")
        );
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    public void serialize(TupleExceptionDetails tupleExceptionDetails, String fileName) {
        // Act
        var json = this.writeValueAsString(tupleExceptionDetails);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(TestsIOUtils.readFile("tuples", fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeTest")
    public void deserializeTest(TupleExceptionDetails tupleExceptionDetails, String fileName) {
        // Arrange
        var json = TestsIOUtils.readFile("tuples", fileName);
        var typeReference = new TypeReference<TupleExceptionDetails>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(tupleExceptionDetails);
    }
}
