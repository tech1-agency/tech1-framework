package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordTest extends AbstractSerializationDeserializationRunner {
    private final static Password PASSWORD = Password.of("admin123!");

    @Override
    protected String getFileName() {
        return "password1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    public void serializeTest() {
        // Act
        var json = this.writeValueAsString(PASSWORD);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    public void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Password>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(PASSWORD);
        assertThat(actual.getValue()).isEqualTo(PASSWORD.getValue());
        assertThat(actual.toString()).isEqualTo(PASSWORD.getValue());
    }
}
