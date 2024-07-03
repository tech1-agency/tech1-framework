package io.tech1.framework.iam.tests.contexts;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.iam.utils.HttpRequestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.iam.handlers"
})
public class TestsApplicationHandlersContext {

    @Bean
    SecurityJwtPublisher securityJwtPublisher() {
        return mock(SecurityJwtPublisher.class);
    }

    @Bean
    SecurityJwtIncidentPublisher securityJwtIncidentPublisher() {
        return mock(SecurityJwtIncidentPublisher.class);
    }

    @Bean
    HttpRequestUtils httpRequestUtility() {
        return mock(HttpRequestUtils.class);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
