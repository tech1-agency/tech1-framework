package io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;

public record SpringBootActuatorInfoGit(
        @JsonProperty("commit") SpringBootActuatorInfoCommit commit,
        @JsonProperty("branch") String branch
) {

    public static SpringBootActuatorInfoGit undefined() {
        return new SpringBootActuatorInfoGit(
                SpringBootActuatorInfoCommit.undefined(),
                UNDEFINED
        );
    }

    public static SpringBootActuatorInfoGit testsHardcoded() {
        return new SpringBootActuatorInfoGit(
                SpringBootActuatorInfoCommit.testsHardcoded(),
                "dev"
        );
    }
}
