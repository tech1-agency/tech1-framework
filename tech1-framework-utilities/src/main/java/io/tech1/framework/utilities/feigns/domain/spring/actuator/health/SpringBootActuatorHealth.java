package io.tech1.framework.utilities.feigns.domain.spring.actuator.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.actuate.health.Status;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SpringBootActuatorHealth {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Status status;

    public static SpringBootActuatorHealth undefinedSpringBootActuatorHealth() {
        return new SpringBootActuatorHealth(
                Status.UNKNOWN
        );
    }
}
