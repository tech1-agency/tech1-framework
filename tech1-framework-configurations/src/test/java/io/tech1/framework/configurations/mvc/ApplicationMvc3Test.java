package io.tech1.framework.configurations.mvc;

import io.tech1.framework.domain.properties.configs.MvcConfigs;
import io.tech1.framework.domain.properties.configs.mvc.CorsConfigs;
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

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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
            var mvcConfigs = MvcConfigs.of(
                    true,
                    "/platform/security",
                    CorsConfigs.of(
                            "/api/**",
                            new String[] { "http://localhost:8080", "http://localhost:8081" },
                            new String[] { "GET", "POST" },
                            new String[] { "Access-Control-Allow-Origin" },
                            true,
                            null
                    )
            );
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
    public void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods).contains("addCorsMappings");
        assertThat(methods).hasSize(28);
    }

    @Test
    public void addCorsMappingsEnabledTest() {
        // Arrange
        var corsRegistry = mock(CorsRegistry.class);
        var corsRegistration = mock(CorsRegistration.class);
        when(corsRegistry.addMapping(eq("/api/**"))).thenReturn(corsRegistration);

        // Act
        this.componentUnderTest.addCorsMappings(corsRegistry);

        // Assert
        verify(corsRegistry).addMapping("/api/**");
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
