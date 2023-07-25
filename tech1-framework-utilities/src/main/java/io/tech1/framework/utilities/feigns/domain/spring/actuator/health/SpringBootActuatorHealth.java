package io.tech1.framework.utilities.feigns.domain.spring.actuator.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.actuate.health.Status;

public record SpringBootActuatorHealth(@JsonInclude(JsonInclude.Include.NON_NULL) Status status) {

    public static SpringBootActuatorHealth undefinedSpringBootActuatorHealth() {
        return new SpringBootActuatorHealth(
                Status.UNKNOWN
        );
    }

}
