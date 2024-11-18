package jbst.foundation.feigns.domain.spring.actuator.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.actuate.health.Status;

public record SpringBootActuatorHealth(
        @JsonInclude(JsonInclude.Include.NON_NULL) Status status
) {

    public static SpringBootActuatorHealth hardcoded() {
        return new SpringBootActuatorHealth(
                Status.UP
        );
    }

    public static SpringBootActuatorHealth unknown() {
        return new SpringBootActuatorHealth(
                Status.UNKNOWN
        );
    }

    public static SpringBootActuatorHealth offline() {
        return unknown();
    }
}
