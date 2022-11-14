package io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SpringBootActuatorInfoCommit {
    @JsonProperty("id")
    private final String id;
    @JsonProperty("time")
    private final String time;

    public static SpringBootActuatorInfoCommit undefinedSpringBootActuatorInfoCommit() {
        return new SpringBootActuatorInfoCommit(
                UNDEFINED,
                UNDEFINED
        );
    }
}
