package io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.tech1.framework.foundation.domain.constants.StringConstants.DASH;

public record SpringBootActuatorInfoGit(
        @JsonProperty("commit") SpringBootActuatorInfoCommit commit,
        @JsonProperty("branch") String branch
) {

    public static SpringBootActuatorInfoGit dash() {
        return new SpringBootActuatorInfoGit(
                SpringBootActuatorInfoCommit.dash(),
                DASH
        );
    }

    public static SpringBootActuatorInfoGit testsHardcoded() {
        return new SpringBootActuatorInfoGit(
                SpringBootActuatorInfoCommit.testsHardcoded(),
                "dev"
        );
    }
}
