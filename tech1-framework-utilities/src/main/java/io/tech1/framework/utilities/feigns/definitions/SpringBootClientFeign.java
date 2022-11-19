package io.tech1.framework.utilities.feigns.definitions;

import feign.RequestLine;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;

public interface SpringBootClientFeign {
    @RequestLine("GET /actuator/info")
    SpringBootActuatorInfo info();

    @RequestLine("GET /actuator/health")
    SpringBootActuatorHealth health();
}
