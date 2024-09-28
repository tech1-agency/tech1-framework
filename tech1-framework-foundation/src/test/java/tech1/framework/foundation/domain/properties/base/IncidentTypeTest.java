package tech1.framework.foundation.domain.properties.base;

import com.fasterxml.jackson.core.type.TypeReference;
import tech1.framework.foundation.domain.tests.runners.AbstractFolderSerializationRunner;
import tech1.framework.foundation.domain.tuples.Tuple1;
import tech1.framework.foundation.domain.tests.io.TestsIOUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType.AUTHENTICATION_LOGIN;
import static tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType.REGISTER1;
import static org.assertj.core.api.Assertions.assertThat;

class IncidentTypeTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(new Tuple1<>(REGISTER1), "incident-type-register1.json"),
                Arguments.of(new Tuple1<>(AUTHENTICATION_LOGIN), "incident-type-authentication-login.json")
        );
    }

    @Override
    protected String getFolder() {
        return "properties";
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    void serialize(Tuple1<SecurityJwtIncidentType> tuple1, String fileName) {
        // Act
        var json = this.writeValueAsString(tuple1);

        // Assert
        assertThat(json).isEqualTo(TestsIOUtils.readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("serializeTest")
    void deserializeTest(Tuple1<SecurityJwtIncidentType> tuple1, String fileName) {
        // Arrange
        var json = TestsIOUtils.readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<Tuple1<SecurityJwtIncidentType>>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isEqualTo(tuple1);
    }
}
