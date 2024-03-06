package io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;

public record SpringBootActuatorInfoCommit(
        @JsonProperty("id") String id,
        @JsonProperty("time") String time
) {

    public static SpringBootActuatorInfoCommit undefined() {
        return new SpringBootActuatorInfoCommit(
                UNDEFINED,
                UNDEFINED
        );
    }

    public static SpringBootActuatorInfoCommit testsHardcoded() {
        return new SpringBootActuatorInfoCommit(
                "1234567",
                "01.01.2024 15:00:00"
        );
    }
}
