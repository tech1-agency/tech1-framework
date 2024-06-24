package io.tech1.framework.foundation.utilities.feigns.clients;

import io.tech1.framework.foundation.domain.base.ServerName;
import io.tech1.framework.foundation.domain.tuples.Tuple2;
import io.tech1.framework.foundation.utilities.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import io.tech1.framework.foundation.utilities.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;

public interface AbstractSpringBootClient {
    ServerName getServerName();
    boolean isAlive();

    SpringBootActuatorInfo info();
    Tuple2<ServerName, SpringBootActuatorInfo> infoMappedByServerName();

    SpringBootActuatorHealth health();
    Tuple2<ServerName, SpringBootActuatorHealth> healthMappedByServerName();
}
