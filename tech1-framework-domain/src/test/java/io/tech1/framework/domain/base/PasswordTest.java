package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest extends AbstractSerializationDeserializationRunner {
    private static final Password PASSWORD = Password.of("admin123!");

    @Override
    protected String getFileName() {
        return "password-1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    void serializeTest() {
        // Act
        var json = this.writeValueAsString(PASSWORD);

        // Assert
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Password>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(PASSWORD);
        assertThat(actual.value()).isEqualTo(PASSWORD.value());
        assertThat(actual.toString()).hasToString(PASSWORD.value());
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomPasswordTest() {
        // Act
        var actual = Password.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isNotNull();
    }
}
