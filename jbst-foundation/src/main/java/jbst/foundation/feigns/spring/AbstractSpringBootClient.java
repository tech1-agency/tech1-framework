package jbst.foundation.feigns.spring;

import jbst.foundation.domain.base.ServerName;
import jbst.foundation.domain.tuples.Tuple2;
import jbst.foundation.feigns.spring.domain.SpringBootActuatorHealth;
import jbst.foundation.feigns.spring.domain.SpringBootActuatorInfo;

public interface AbstractSpringBootClient {
    ServerName getServerName();
    boolean isAlive();

    SpringBootActuatorInfo info();
    Tuple2<ServerName, SpringBootActuatorInfo> infoMappedByServerName();

    SpringBootActuatorHealth health();
    Tuple2<ServerName, SpringBootActuatorHealth> healthMappedByServerName();
}
