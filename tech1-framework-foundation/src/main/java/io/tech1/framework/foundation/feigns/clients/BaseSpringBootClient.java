package io.tech1.framework.foundation.feigns.clients;

import feign.RetryableException;
import io.tech1.framework.foundation.domain.base.ServerName;
import io.tech1.framework.foundation.domain.tuples.Tuple2;
import io.tech1.framework.foundation.feigns.definitions.SpringBootClientFeign;
import io.tech1.framework.foundation.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import io.tech1.framework.foundation.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.foundation.domain.constants.LogsConstants.SERVER_OFFLINE;
import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
public abstract class BaseSpringBootClient implements AbstractSpringBootClient {

    protected final SpringBootClientFeign springBootClientFeign;

    @Override
    public boolean isAlive() {
        var info = this.info();
        return nonNull(info) && !SpringBootActuatorInfo.offline().equals(info);
    }

    @Override
    public SpringBootActuatorInfo info() {
        try {
            return this.springBootClientFeign.info();
        } catch (RetryableException ex) {
            LOGGER.error(SERVER_OFFLINE, this.getServerName(), ex.getMessage());
            return SpringBootActuatorInfo.offline();
        }
    }

    @Override
    public Tuple2<ServerName, SpringBootActuatorInfo> infoMappedByServerName() {
        return new Tuple2<>(this.getServerName(), this.info());
    }

    @Override
    public SpringBootActuatorHealth health() {
        try {
            return this.springBootClientFeign.health();
        } catch (RetryableException ex) {
            LOGGER.error(SERVER_OFFLINE, this.getServerName(), ex.getMessage());
            return SpringBootActuatorHealth.offline();
        }
    }

    @Override
    public Tuple2<ServerName, SpringBootActuatorHealth> healthMappedByServerName() {
        return new Tuple2<>(this.getServerName(), this.health());
    }
}
