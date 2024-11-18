package jbst.foundation.feigns.clients;

import jbst.foundation.domain.base.ServerName;
import jbst.foundation.domain.tuples.Tuple2;
import jbst.foundation.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import jbst.foundation.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;

public interface AbstractSpringBootClient {
    ServerName getServerName();
    boolean isAlive();

    SpringBootActuatorInfo info();
    Tuple2<ServerName, SpringBootActuatorInfo> infoMappedByServerName();

    SpringBootActuatorHealth health();
    Tuple2<ServerName, SpringBootActuatorHealth> healthMappedByServerName();
}
