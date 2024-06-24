package io.tech1.framework.foundation.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.foundation.domain.tests.runners.AbstractSerializationDeserializationRunner;
import io.tech1.framework.foundation.domain.tests.constants.TestsJunitConstants;
import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimestampTest extends AbstractSerializationDeserializationRunner {
    private static final Timestamp TIMESTAMP = Timestamp.testsHardcoded();

    @Override
    protected String getFileName() {
        return "timestamp-1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    void serializeTest() {
        // Act
        var json = this.writeValueAsString(TIMESTAMP);

        // Assert
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Timestamp>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(TIMESTAMP);
        assertThat(actual.value()).isEqualTo(TIMESTAMP.value());
    }

    @RepeatedTest(TestsJunitConstants.SMALL_ITERATIONS_COUNT)
    void randomTest() {
        // Act
        var actual = Timestamp.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isNotZero();
    }
}
