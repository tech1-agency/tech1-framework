package jbst.foundation.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import jbst.foundation.domain.constants.JbstConstants;

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
                JbstConstants.Symbols.DASH,
                JbstConstants.Symbols.DASH
        );
    }
}
