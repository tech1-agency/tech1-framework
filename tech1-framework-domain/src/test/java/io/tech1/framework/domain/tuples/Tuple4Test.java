package io.tech1.framework.domain.tuples;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Tuple4Test extends AbstractTupleTest {
    private final static Tuple4<String, String, String, String> TUPLE = Tuple4.of(
            "1st",
            "2nd",
            "3rd",
            "4th"
    );

    @Override
    protected String getFileName() {
        return "tuple4.json";
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
        var typeReference = new TypeReference<Tuple4<String, String, String, String>>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(TUPLE);
    }
}
