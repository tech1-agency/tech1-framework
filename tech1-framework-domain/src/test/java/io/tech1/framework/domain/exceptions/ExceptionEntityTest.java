package io.tech1.framework.domain.exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractObjectMapperRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.tech1.framework.domain.exceptions.ExceptionEntityType.ERROR;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionEntityTest extends AbstractObjectMapperRunner {

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Test
    void serializeDeserializeTest() {
        // Arrange
        var exceptionMessage = randomString();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
        var arrangedExceptionEntity = ExceptionEntity.of(new NullPointerException(exceptionMessage));
        arrangedExceptionEntity.addAttribute("externalAttribute", randomString());

        // Act
        var json = this.writeValueAsString(arrangedExceptionEntity);
        HashMap<String, Object> exceptionEntity = OBJECT_MAPPER.readValue(json, typeRef);

        // Assert
        assertThat(exceptionEntity).hasSize(3);
        assertThat(exceptionEntity).hasSize(3);
        assertThat(exceptionEntity).containsKeys("exceptionEntityType", "attributes", "timestamp");
        assertThat(exceptionEntity.get("exceptionEntityType")).isEqualTo(ERROR.toString());
        assertThat(exceptionEntity.get("timestamp")).isNotNull();
        var attributes = (Map<String, Object>) exceptionEntity.get("attributes");
        assertThat(attributes).hasSize(3);
        assertThat(attributes.get("shortMessage")).isEqualTo(exceptionMessage);
        assertThat(attributes.get("fullMessage")).isEqualTo(exceptionMessage);
        assertThat(attributes).containsKey("externalAttribute");
    }
}
