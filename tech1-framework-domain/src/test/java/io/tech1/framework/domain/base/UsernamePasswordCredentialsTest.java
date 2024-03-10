package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

class UsernamePasswordCredentialsTest extends AbstractSerializationDeserializationRunner {
    private static final UsernamePasswordCredentials CREDENTIALS = UsernamePasswordCredentials.testsHardcoded();

    @Override
    protected String getFileName() {
        return "username-password-credentials-1.json";
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @Test
    void serializeTest() {
        // Act
        var json = this.writeValueAsString(CREDENTIALS);

        // Assert
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<UsernamePasswordCredentials>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(CREDENTIALS);
        assertThat(actual.username()).isEqualTo(Username.testsHardcoded());
        assertThat(actual.password()).isEqualTo(Password.testsHardcoded());
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomTest() {
        // Act
        var actual = UsernamePasswordCredentials.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.username()).isNotNull();
        assertThat(actual.password()).isNotNull();
    }
}
