package io.tech1.framework.iam.server.postgres.properties;

import io.tech1.framework.iam.server.postgres.properties.server.ServerConfigs;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

@SuppressWarnings("ConfigurationProperties")
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
