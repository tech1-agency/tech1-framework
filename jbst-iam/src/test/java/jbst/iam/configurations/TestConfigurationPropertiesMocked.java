package jbst.iam.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.domain.properties.configs.MvcConfigs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestConfigurationPropertiesMocked {

    @Bean
    JbstProperties applicationFrameworkProperties() {
        var applicationFrameworkProperties = mock(JbstProperties.class);
        when(applicationFrameworkProperties.getMvcConfigs()).thenReturn(MvcConfigs.hardcoded());
        return applicationFrameworkProperties;
    }
}
