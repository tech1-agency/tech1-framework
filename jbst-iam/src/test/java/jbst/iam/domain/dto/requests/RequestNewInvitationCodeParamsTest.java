package jbst.iam.domain.dto.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import jbst.foundation.domain.tests.runners.AbstractSerializationDeserializationRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestNewInvitationCodeParamsTest extends AbstractSerializationDeserializationRunner {
    private static final RequestNewInvitationCodeParams REQUEST = new RequestNewInvitationCodeParams(
            Set.of("admin", "user")
    );

    @Override
    protected String getFolder() {
        return "dto/requests";
    }

    @Override
    protected String getFileName() {
        return "new-invitation-code-params-1.json";
    }

    // serialization is not required for request-based dtos

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<RequestNewInvitationCodeParams>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(REQUEST);
    }
}
