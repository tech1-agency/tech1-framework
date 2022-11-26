package io.tech1.framework.domain.triggers;

import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CronTriggerTest extends AbstractSerializationDeserializationRunner {
    private static final CronTrigger CRON_TRIGGER = CronTrigger.of();

    @Override
    protected String getFileName() {
        return "trigger-cron.json";
    }

    @Override
    protected String getFolder() {
        return "triggers";
    }

    @Test
    public void serializeTest() {
        // Act
        var json = this.writeValueAsString(CRON_TRIGGER);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(this.readFile());
    }
}
