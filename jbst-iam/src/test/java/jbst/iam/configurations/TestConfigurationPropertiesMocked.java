package jbst.iam.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.domain.properties.configs.MvcConfigs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestConfigurationPropertiesMocked {

    @Bean
    ApplicationFrameworkProperties applicationFrameworkProperties() {
        var applicationFrameworkProperties = mock(ApplicationFrameworkProperties.class);
        when(applicationFrameworkProperties.getMvcConfigs()).thenReturn(MvcConfigs.testsHardcoded());
        return applicationFrameworkProperties;
    }
}
