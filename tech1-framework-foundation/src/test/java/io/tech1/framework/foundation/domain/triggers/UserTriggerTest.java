package io.tech1.framework.foundation.domain.triggers;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.tests.runners.AbstractSerializationDeserializationRunner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTriggerTest extends AbstractSerializationDeserializationRunner {
    private static final UserTrigger USER_TRIGGER = new UserTrigger(Username.testsHardcoded());

    @Override
    protected String getFileName() {
        return "trigger-user.json";
    }

    @Override
    protected String getFolder() {
        return "triggers";
    }

    @Test
    void serializeTest() {
        // Act
        var json = this.writeValueAsString(USER_TRIGGER);

        // Assert
        assertThat(json).isEqualTo(this.readFile());
    }
}
