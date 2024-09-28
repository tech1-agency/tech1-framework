package io.tech1.framework.iam.domain.dto.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestUserLoginTest extends AbstractSerializationDeserializationRunner {
    private static final RequestUserLogin REQUEST = new RequestUserLogin(
            Username.testsHardcoded(),
            Password.of("password123")
    );

    @Override
    protected String getFolder() {
        return "dto/requests";
    }

    @Override
    protected String getFileName() {
        return "user-login-1.json";
    }

    // serialization is not required for request-based dtos

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<RequestUserLogin>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(REQUEST);
    }
}
