package io.tech1.framework.b2b.base.security.jwt.configurations;

import io.tech1.framework.b2b.base.security.jwt.tests.classes.ClassAnnotatedAbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.tests.classes.ClassNotAnnotatedAbstractFrameworkBaseSecurityResource;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApplicationBaseSecurityJwtMvcTest {

    @Configuration
    @Import(
            ApplicationFrameworkPropertiesTestsHardcodedContext.class
    )
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        ApplicationBaseSecurityJwtMvc applicationMVC() {
            return new ApplicationBaseSecurityJwtMvc(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final ApplicationBaseSecurityJwtMvc componentUnderTest;

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

    @SuppressWarnings("unchecked")
    @Test
    void configurePathMatchTest() {
        // Arrange
        var configurer = mock(PathMatchConfigurer.class);

        // Act
        this.componentUnderTest.configurePathMatch(configurer);

        // Assert
        var prefixAC = ArgumentCaptor.forClass(String.class);
        var predicateAC = ArgumentCaptor.forClass(Predicate.class);
        verify(configurer).addPathPrefix(prefixAC.capture(), predicateAC.capture());
        assertThat(prefixAC.getValue()).isEqualTo("/framework/security");
        Predicate<Class<?>> predicate = predicateAC.getValue();
        assertThat(predicate.test(ClassAnnotatedAbstractFrameworkBaseSecurityResource.class)).isTrue();
        assertThat(predicate.test(ClassNotAnnotatedAbstractFrameworkBaseSecurityResource.class)).isFalse();
    }
}
