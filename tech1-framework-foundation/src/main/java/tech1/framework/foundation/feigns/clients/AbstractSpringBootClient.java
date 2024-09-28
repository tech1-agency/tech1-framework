package tech1.framework.foundation.feigns.clients;

import tech1.framework.foundation.domain.base.ServerName;
import tech1.framework.foundation.domain.tuples.Tuple2;
import tech1.framework.foundation.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import tech1.framework.foundation.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;

public interface AbstractSpringBootClient {
    ServerName getServerName();
    boolean isAlive();

    SpringBootActuatorInfo info();
    Tuple2<ServerName, SpringBootActuatorInfo> infoMappedByServerName();

    SpringBootActuatorHealth health();
    Tuple2<ServerName, SpringBootActuatorHealth> healthMappedByServerName();
}
