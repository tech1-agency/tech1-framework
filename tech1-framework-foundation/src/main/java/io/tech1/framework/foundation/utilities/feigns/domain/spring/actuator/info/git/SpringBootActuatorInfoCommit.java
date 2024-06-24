package io.tech1.framework.foundation.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.tech1.framework.foundation.domain.constants.StringConstants.DASH;

public record SpringBootActuatorInfoCommit(
        @JsonProperty("id") String id,
        @JsonProperty("time") String time
) {

    public static SpringBootActuatorInfoCommit dash() {
        return new SpringBootActuatorInfoCommit(
                DASH,
                DASH
        );
    }

    public static SpringBootActuatorInfoCommit testsHardcoded() {
        return new SpringBootActuatorInfoCommit(
                "1234567",
                "01.01.2024 15:00:00"
        );
    }
}
