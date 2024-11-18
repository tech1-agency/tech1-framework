package tech1.framework.foundation.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import tech1.framework.foundation.domain.constants.JbsConstants;
import tech1.framework.foundation.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest extends AbstractSerializationDeserializationRunner {
    private static final Email EMAIL = Email.testsHardcoded();

    @Override
    protected String getFileName() {
        return "email-1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    void serializeTest() {
        // Act
        var json = this.writeValueAsString(EMAIL);

        // Assert
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Email>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(EMAIL);
        assertThat(actual.value()).isEqualTo(EMAIL.value());
        assertThat(actual.toString()).hasToString(EMAIL.value());
    }

    @Test
    void randomTest() {
        // Arrange
        var randomLength = 32;
        var domainLength = 14;
        var expected = randomLength + domainLength;

        // Act
        var actual = Email.random();

        // Assert
        assertThat(actual.value()).hasSize(expected);
        assertThat(actual.value().substring(randomLength)).isEqualTo("@" + JbsConstants.Domains.HARDCODED);
    }
}
