package jbst.iam.configurations;

import jbst.iam.annotations.AbstractJbstBaseSecurityResource;
import jbst.foundation.configurations.ConfigurationWebMVC;
import jbst.foundation.domain.properties.JbstProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

@Configuration
public class ConfigurationBaseSecurityJwtWebMvc extends ConfigurationWebMVC {

    @Autowired
    public ConfigurationBaseSecurityJwtWebMvc(JbstProperties jbstProperties) {
        super(jbstProperties);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        var basePathPrefix = this.jbstProperties.getMvcConfigs().getBasePathPrefix();
        configurer.addPathPrefix(basePathPrefix, resource -> resource.isAnnotationPresent(AbstractJbstBaseSecurityResource.class));
    }
}
