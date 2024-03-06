package io.tech1.framework.domain.triggers;

import io.tech1.framework.domain.tests.runners.AbstractObjectMapperRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class TriggerTypeTest  extends AbstractObjectMapperRunner {

    @ParameterizedTest
    @EnumSource(TriggerType.class)
    void serializeTest(TriggerType state) {
        // Act
        var json = this.writeValueAsString(state).replace("\"", "");

        // Assert
        assertThat(json).isEqualTo(state.getValue());
    }
}
