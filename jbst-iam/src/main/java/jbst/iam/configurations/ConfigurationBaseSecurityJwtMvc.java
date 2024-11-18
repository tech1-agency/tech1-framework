package jbst.iam.configurations;

import jbst.iam.annotations.AbstractJbstBaseSecurityResource;
import tech1.framework.foundation.configurations.ApplicationMVC;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

@Configuration
public class ConfigurationBaseSecurityJwtMvc extends ApplicationMVC {

    @Autowired
    public ConfigurationBaseSecurityJwtMvc(ApplicationFrameworkProperties applicationFrameworkProperties) {
        super(applicationFrameworkProperties);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        var basePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();
        configurer.addPathPrefix(basePathPrefix, resource -> resource.isAnnotationPresent(AbstractJbstBaseSecurityResource.class));
    }
}
