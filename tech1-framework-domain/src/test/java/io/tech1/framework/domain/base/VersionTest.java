package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionTest extends AbstractSerializationDeserializationRunner {
    private final static Version VERSION = Version.of("1.1");

    @Override
    protected String getFileName() {
        return "version1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    public void serializeTest() {
        // Act
        var json = this.writeValueAsString(VERSION);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    public void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Version>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(VERSION);
    }
}
