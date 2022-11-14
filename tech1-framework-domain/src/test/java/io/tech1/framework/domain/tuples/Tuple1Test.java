package io.tech1.framework.domain.tuples;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Tuple1Test extends AbstractTupleTest {
    private final static Tuple1<String> TUPLE = Tuple1.of(
            "1st"
    );

    @Override
    protected String getFileName() {
        return "tuple1.json";
    }

    @Test
    public void serializeTest() {
        // Act
        var json = this.writeValueAsString(TUPLE);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(this.readFile());
    }

    @SneakyThrows
    @Test
    public void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<Tuple1<String>>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(TUPLE);
    }
}
