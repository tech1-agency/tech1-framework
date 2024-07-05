package io.tech1.framework.iam.server.mongodb.properties;

import io.tech1.framework.iam.server.mongodb.properties.server.ServerConfigs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

@Slf4j
@Profile("mongodb")
@ConfigurationProperties(
        prefix = "tech1-server",
        ignoreUnknownFields = false
)
@Data
public class ApplicationProperties implements PriorityOrdered {
    private ServerConfigs serverConfigs;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
