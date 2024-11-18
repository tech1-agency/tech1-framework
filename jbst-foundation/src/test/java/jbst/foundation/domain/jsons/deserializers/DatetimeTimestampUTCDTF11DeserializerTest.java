package jbst.foundation.domain.jsons.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatetimeTimestampUTCDTF11DeserializerTest {
    private final DatetimeTimestampUTCDTF11Deserializer componentUnderTest = new DatetimeTimestampUTCDTF11Deserializer();

    @Test
    void deserialize() throws IOException {
        // Arrange
        var deserializationContext = mock(DeserializationContext.class);
        var jsonParser = mock(JsonParser.class);
        when(jsonParser.getText()).thenReturn("25-12-2023 15:30:00");

        // Act
        var actual = this.componentUnderTest.deserialize(jsonParser, deserializationContext);

        // Assert
        assertThat(actual).isEqualTo(1703518200000L);
    }
}
