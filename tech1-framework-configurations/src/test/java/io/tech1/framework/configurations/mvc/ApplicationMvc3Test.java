package io.tech1.framework.configurations.mvc;

import io.tech1.framework.domain.tests.constants.TestsPropertiesConstants;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationMvc3Test {

    @Configuration
    static class ContextConfiguration {
        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            var applicationFrameworkProperties = mock(ApplicationFrameworkProperties.class);
            var mvcConfigs = TestsPropertiesConstants.MVC_CONFIGS;
            mvcConfigs.getCorsConfigs().setExposedHeaders(new String[] { "Content-Type" });
            when(applicationFrameworkProperties.getMvcConfigs()).thenReturn(mvcConfigs);
            return applicationFrameworkProperties;
        }

        @Bean
        ApplicationMVC applicationMVC() {
            return new ApplicationMVC(
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final ApplicationMVC componentUnderTest;

    @Test
    public void addCorsMappingsDisabled() {
        // Arrange
        var corsRegistry = mock(CorsRegistry.class);
        var corsRegistration = mock(CorsRegistration.class);
        when(corsRegistry.addMapping(eq("/api/**"))).thenReturn(corsRegistration);

        // Act
        this.componentUnderTest.addCorsMappings(corsRegistry);

        // Assert
        verify(corsRegistry).addMapping(eq("/api/**"));
        verify(corsRegistration).allowedOrigins("http://localhost:8080", "http://localhost:8081");
        verify(corsRegistration).allowedMethods("GET", "POST" );
        verify(corsRegistration).allowedHeaders("Access-Control-Allow-Origin");
        verify(corsRegistration).exposedHeaders("Content-Type");
        verify(corsRegistration).allowCredentials(true);
        verifyNoMoreInteractions(
                corsRegistry,
                corsRegistration
        );
    }
}
