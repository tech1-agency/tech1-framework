package io.tech1.framework.domain.tuples;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class Tuple2Test extends AbstractTupleTest {
    private final static Tuple2<String, String> TUPLE = Tuple2.of(
            "1st",
            "2nd"
    );

    @Override
    protected String getFileName() {
        return "tuple2.json";
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
        var typeReference = new TypeReference<Tuple2<String, String>>() {};

        // Act
        var tuple = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(tuple).isNotNull();
        assertThat(tuple).isEqualTo(TUPLE);
    }
}