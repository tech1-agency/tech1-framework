package jbst.iam.tests.contexts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.utils.HttpRequestUtils;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "tech1.framework.iam.handlers"
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
