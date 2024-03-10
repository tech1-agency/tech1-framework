package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

class ServerNameTest extends AbstractSerializationDeserializationRunner {
    private static final ServerName SERVER_NAME = ServerName.testsHardcoded();

    @Override
    protected String getFileName() {
        return "server-name-1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    void serializeTest() {
        // Act
        var json = this.writeValueAsString(SERVER_NAME);

        // Assert
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<ServerName>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(SERVER_NAME);
        assertThat(actual.value()).isEqualTo(SERVER_NAME.value());
        assertThat(actual.toString()).hasToString(SERVER_NAME.value());
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomTest() {
        // Act
        var actual = ServerName.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isNotNull();
    }
}
