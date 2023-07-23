package io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoCommit.undefinedSpringBootActuatorInfoCommit;

public record SpringBootActuatorInfoGit(
        @JsonProperty("commit") SpringBootActuatorInfoCommit commit,
        @JsonProperty("branch") String branch
) {

    public static SpringBootActuatorInfoGit undefinedSpringBootActuatorInfoGit() {
        return new SpringBootActuatorInfoGit(
                undefinedSpringBootActuatorInfoCommit(),
                UNDEFINED
        );
    }

}
