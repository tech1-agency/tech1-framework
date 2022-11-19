package io.tech1.framework.utilities.feigns.clients;

import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;

public interface AbstractSpringBootClient {
    String getServerName();
    boolean isAlive();

    SpringBootActuatorInfo info();
    Tuple2<String, SpringBootActuatorInfo> infoMappedByServerName();

    SpringBootActuatorHealth health();
    Tuple2<String, SpringBootActuatorHealth> healthMappedByServerName();
}
