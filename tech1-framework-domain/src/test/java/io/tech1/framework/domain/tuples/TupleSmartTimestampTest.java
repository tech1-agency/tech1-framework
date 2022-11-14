package io.tech1.framework.domain.tuples;

import io.tech1.framework.domain.tests.constants.TestsConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TupleSmartTimestampTest extends AbstractTupleTest {
    private final static TupleSmartTimestamp TUPLE = TupleSmartTimestamp.of(
            1668419401637L,
            TestsConstants.EET_ZONE_ID,
            TestsConstants.DEFAULT_DATE_FORMAT_PATTERN
    );

    @Override
    protected String getFileName() {
        return "tuple-smart-timestamp.json";
    }

    @Test
    public void serializeTest() {
        // Act
        var json = this.writeValueAsString(TUPLE);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(this.readFile());
    }

    // deserialized ignored deliberately
}
