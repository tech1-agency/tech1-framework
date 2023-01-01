package io.tech1.framework.incidents.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import io.tech1.framework.domain.tuples.Tuple1;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

public class IncidentTypeTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(Tuple1.of(IncidentType.THROWABLE), "incident-type-throwable.json"),
                Arguments.of(Tuple1.of(IncidentType.AUTHENTICATION_LOGIN), "incident-type-authentication-login.json")
        );
    }

    @Override
    protected String getFolder() {
        return "incidents";
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    public void serialize(Tuple1<IncidentType> tuple1, String fileName) {
        // Act
        var json = this.writeValueAsString(tuple1);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeTest")
    public void deserializeTest(Tuple1<IncidentType> tuple1, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<Tuple1<IncidentType>>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(tuple1);
    }
}
