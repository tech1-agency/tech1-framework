package io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoCommit.undefinedSpringBootActuatorInfoCommit;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SpringBootActuatorInfoGit {
    @JsonProperty("commit")
    private final SpringBootActuatorInfoCommit commit;
    @JsonProperty("branch")
    private final String branch;

    public static SpringBootActuatorInfoGit undefinedSpringBootActuatorInfoGit() {
        return new SpringBootActuatorInfoGit(
                undefinedSpringBootActuatorInfoCommit(),
                UNDEFINED
        );
    }
}
