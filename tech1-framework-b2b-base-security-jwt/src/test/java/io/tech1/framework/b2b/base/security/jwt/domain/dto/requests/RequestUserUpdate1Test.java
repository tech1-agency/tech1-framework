package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.tests.constants.TestsConstants.EET_ZONE_ID;
import static io.tech1.framework.domain.tests.constants.TestsConstants.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

class RequestUserUpdate1Test extends AbstractSerializationDeserializationRunner {
    private static final RequestUserUpdate1 REQUEST = new RequestUserUpdate1(
            EET_ZONE_ID.getId(),
            EMAIL,
            "Tech1 Tests"
    );

    @Override
    protected String getFolder() {
        return "dto/requests";
    }

    @Override
    protected String getFileName() {
        return "user-update-1.json";
    }

    // serialization is not required for request-based dtos

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<RequestUserUpdate1>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(REQUEST);
    }
}
