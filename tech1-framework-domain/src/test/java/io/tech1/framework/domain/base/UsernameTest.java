package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

class UsernameTest extends AbstractSerializationDeserializationRunner {
    private static final Username USERNAME = Username.of("tech1");

    @Override
    protected String getFileName() {
        return "username-1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    void serializeTest() {
        // Act
        var json = this.writeValueAsString(USERNAME);

        // Assert
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Username>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(USERNAME);
        assertThat(actual.identifier()).isEqualTo(USERNAME.identifier());
        assertThat(actual.toString()).hasToString(USERNAME.identifier());
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomTest() {
        // Act
        var actual = Username.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.identifier()).isNotNull();
    }
}
