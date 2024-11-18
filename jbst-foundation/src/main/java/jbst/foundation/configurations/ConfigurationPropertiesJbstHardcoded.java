package jbst.foundation.configurations;

import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationPropertiesJbstHardcoded {

    @Bean
    public ApplicationFrameworkProperties applicationFrameworkProperties() {
        return ApplicationFrameworkProperties.hardcoded();
    }
}
