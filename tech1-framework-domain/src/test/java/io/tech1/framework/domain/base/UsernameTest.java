package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsernameTest extends AbstractSerializationDeserializationTest {
    private final static Username USERNAME = Username.of("tech1");

    @Override
    protected String getFileName() {
        return "username1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    public void serializeTest() {
        // Act
        var json = this.writeValueAsString(USERNAME);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    public void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Username>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(USERNAME);
    }
}
