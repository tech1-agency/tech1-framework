package jbst.foundation.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import jbst.foundation.domain.constants.JbstConstants;

public record SpringBootActuatorInfoGit(
        @JsonProperty("commit") SpringBootActuatorInfoCommit commit,
        @JsonProperty("branch") String branch
) {

    public static SpringBootActuatorInfoGit hardcoded() {
        return new SpringBootActuatorInfoGit(
                SpringBootActuatorInfoCommit.hardcoded(),
                "dev"
        );
    }

    public static SpringBootActuatorInfoGit dash() {
        return new SpringBootActuatorInfoGit(
                SpringBootActuatorInfoCommit.dash(),
                JbstConstants.Symbols.DASH
        );
    }
}
