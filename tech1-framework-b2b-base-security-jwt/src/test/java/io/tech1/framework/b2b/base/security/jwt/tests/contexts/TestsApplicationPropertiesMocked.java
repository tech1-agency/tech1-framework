package io.tech1.framework.b2b.base.security.jwt.tests.contexts;

import io.tech1.framework.domain.tests.constants.TestsPropertiesConstants;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestsApplicationPropertiesMocked {

    @Bean
    public ApplicationFrameworkProperties applicationFrameworkProperties() {
        var applicationFrameworkProperties = mock(ApplicationFrameworkProperties.class);
        when(applicationFrameworkProperties.getMvcConfigs()).thenReturn(TestsPropertiesConstants.MVC_CONFIGS);
        return applicationFrameworkProperties;
    }
}
