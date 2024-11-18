package jbst.foundation.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;

import static jbst.foundation.domain.constants.StringConstants.DASH;

public record SpringBootActuatorInfoCommit(
        @JsonProperty("id") String id,
        @JsonProperty("time") String time
) {

    public static SpringBootActuatorInfoCommit hardcoded() {
        return new SpringBootActuatorInfoCommit(
                "1234567",
                "01.01.2024 15:00:00"
        );
    }

    public static SpringBootActuatorInfoCommit dash() {
        return new SpringBootActuatorInfoCommit(
                DASH,
                DASH
        );
    }
}
