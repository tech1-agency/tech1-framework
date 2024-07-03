package io.tech1.framework.b2b.base.security.jwt.tests.contexts;

import io.tech1.framework.foundation.domain.properties.configs.MvcConfigs;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestsApplicationPropertiesMocked {

    @Bean
    ApplicationFrameworkProperties applicationFrameworkProperties() {
        var applicationFrameworkProperties = mock(ApplicationFrameworkProperties.class);
        when(applicationFrameworkProperties.getMvcConfigs()).thenReturn(MvcConfigs.testsHardcoded());
        return applicationFrameworkProperties;
    }
}
