package io.tech1.framework.iam.server.postgres.properties;

import io.tech1.framework.iam.server.postgres.properties.server.PostgresServerConfigs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

@Slf4j
@Profile("postgres")
@ConfigurationProperties(
        prefix = "tech1-server",
        ignoreUnknownFields = false
)
@Data
public class ApplicationProperties implements PriorityOrdered {
    private PostgresServerConfigs serverConfigs;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
