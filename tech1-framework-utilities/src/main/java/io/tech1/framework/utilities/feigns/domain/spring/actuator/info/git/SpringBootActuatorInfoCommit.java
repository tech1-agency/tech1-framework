package io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;

public record SpringBootActuatorInfoCommit(
        @JsonProperty("id") String id,
        @JsonProperty("time") String time
) {

    public static SpringBootActuatorInfoCommit undefinedSpringBootActuatorInfoCommit() {
        return new SpringBootActuatorInfoCommit(
                UNDEFINED,
                UNDEFINED
        );
    }

}
