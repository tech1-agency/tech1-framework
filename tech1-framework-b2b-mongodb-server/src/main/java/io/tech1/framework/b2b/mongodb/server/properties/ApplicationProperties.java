package io.tech1.framework.b2b.mongodb.server.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

@Slf4j
@ConfigurationProperties(
        prefix = "app",
        ignoreUnknownFields = false
)
@Data
public class ApplicationProperties implements PriorityOrdered {
    private String attribute1;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
