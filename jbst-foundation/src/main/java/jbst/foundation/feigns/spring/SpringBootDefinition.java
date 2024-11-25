package jbst.foundation.feigns.spring;

import feign.RequestLine;
import jbst.foundation.feigns.spring.domain.SpringBootActuatorHealth;
import jbst.foundation.feigns.spring.domain.SpringBootActuatorInfo;

public interface SpringBootDefinition {
    @RequestLine("GET /actuator/info")
    SpringBootActuatorInfo info();

    @RequestLine("GET /actuator/health")
    SpringBootActuatorHealth health();
}
