package io.tech1.framework.b2b.mongodb.security.jwt.configurations;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApplicationSecurityJwtMvcTest {

    @Configuration
    @Import(
            ApplicationFrameworkPropertiesContext.class
    )
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        ApplicationSecurityJwtMvc applicationMVC() {
            return new ApplicationSecurityJwtMvc(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final ApplicationSecurityJwtMvc componentUnderTest;

    @Test
    void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods)
                .hasSize(28)
                .contains("addCorsMappings")
                .contains("configurePathMatch");
    }
}
