package io.tech1.framework.domain.triggers;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTriggerTest extends AbstractSerializationDeserializationRunner {
    private final static UserTrigger USER_TRIGGER = new UserTrigger(Username.of("tech1"));

    @Override
    protected String getFileName() {
        return "trigger-user.json";
    }

    @Override
    protected String getFolder() {
        return "triggers";
    }

    @Test
    public void serializeTest() {
        // Act
        var json = this.writeValueAsString(USER_TRIGGER);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(this.readFile());
    }
}
