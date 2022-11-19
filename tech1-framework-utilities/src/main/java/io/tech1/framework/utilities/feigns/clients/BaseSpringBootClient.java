package io.tech1.framework.utilities.feigns.clients;

import feign.RetryableException;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.utilities.feigns.definitions.SpringBootClientFeign;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.LogsConstants.SERVER_OFFLINE;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.health.SpringBootActuatorHealth.undefinedSpringBootActuatorHealth;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.info.SpringBootActuatorInfo.undefinedSpringBootActuatorInfo;
import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
public abstract class BaseSpringBootClient implements AbstractSpringBootClient {

    protected final SpringBootClientFeign springBootClientFeign;

    @Override
    public boolean isAlive() {
        var info = this.info();
        return nonNull(info) && !undefinedSpringBootActuatorInfo().equals(info);
    }

    @Override
    public SpringBootActuatorInfo info() {
        try {
            return this.springBootClientFeign.info();
        } catch (RetryableException ex) {
            LOGGER.warn(SERVER_OFFLINE, this.getServerName(), ex.getMessage());
            return undefinedSpringBootActuatorInfo();
        }
    }

    @Override
    public Tuple2<String, SpringBootActuatorInfo> infoMappedByServerName() {
        return Tuple2.of(this.getServerName(), this.info());
    }

    @Override
    public SpringBootActuatorHealth health() {
        try {
            return this.springBootClientFeign.health();
        } catch (RetryableException ex) {
            LOGGER.warn(SERVER_OFFLINE, this.getServerName(), ex.getMessage());
            return undefinedSpringBootActuatorHealth();
        }
    }

    @Override
    public Tuple2<String, SpringBootActuatorHealth> healthMappedByServerName() {
        return Tuple2.of(this.getServerName(), this.health());
    }
}
