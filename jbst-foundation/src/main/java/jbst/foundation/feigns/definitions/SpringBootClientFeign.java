package jbst.foundation.feigns.definitions;

import feign.RequestLine;
import jbst.foundation.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import jbst.foundation.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;

public interface SpringBootClientFeign {
    @RequestLine("GET /actuator/info")
    SpringBootActuatorInfo info();

    @RequestLine("GET /actuator/health")
    SpringBootActuatorHealth health();
}
