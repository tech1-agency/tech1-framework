package io.tech1.framework.configurations.mvc;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_CORS_PREFIX;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printProperties;
import static java.util.Objects.nonNull;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationMVC implements WebMvcConfigurer {

    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        var mvcConfigs = this.applicationFrameworkProperties.getMvcConfigs();
        assertProperties(mvcConfigs, "mvcConfigs");
        printProperties(mvcConfigs, FRAMEWORK_CORS_PREFIX);
    }

    @Override
    public void addCorsMappings(@NotNull CorsRegistry corsRegistry) {
        var mvcConfigs = this.applicationFrameworkProperties.getMvcConfigs();
        if (mvcConfigs.isEnabled()) {
            var corsConfigs = mvcConfigs.getCorsConfigs();

            var pathPattern = corsConfigs.getPathPattern();
            var corsRegistration = corsRegistry.addMapping(pathPattern);

            var allowedOrigins = corsConfigs.getAllowedOrigins();
            if (nonNull(allowedOrigins)) {
                corsRegistration.allowedOrigins(allowedOrigins);
            }

            var allowedMethods = corsConfigs.getAllowedMethods();
            if (nonNull(allowedMethods)) {
                corsRegistration.allowedMethods(allowedMethods);
            }

            var allowedHeaders = corsConfigs.getAllowedHeaders();
            if (nonNull(allowedHeaders)) {
                corsRegistration.allowedHeaders(allowedHeaders);
            }

            var exposedHeaders = corsConfigs.getExposedHeaders();
            if (nonNull(exposedHeaders)) {
                corsRegistration.exposedHeaders(exposedHeaders);
            }

            var allowCredentials = corsConfigs.isAllowCredentials();
            corsRegistration.allowCredentials(allowCredentials);
        }
    }
}
