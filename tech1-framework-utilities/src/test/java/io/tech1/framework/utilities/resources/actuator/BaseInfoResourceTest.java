package io.tech1.framework.utilities.resources.actuator;

import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Map;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseInfoResourceTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesTestsHardcodedContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        EnvironmentUtility environmentUtility() {
            return mock(EnvironmentUtility.class);
        }

        @Bean
        BaseInfoResource baseInfoResource() {
            return new BaseInfoResource(
                    this.environmentUtility(),
                    this.applicationFrameworkProperties
            );
        }
    }

    // Utilities
    private final EnvironmentUtility environmentUtility;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final BaseInfoResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.environmentUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.environmentUtility
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void verifyProfilesConfigurationExceptionTest() {
        // Arrange
        var activeProfile = randomString();
        var builder = mock(Info.Builder.class);
        when(this.environmentUtility.getOneActiveProfileOrDash()).thenReturn(activeProfile);

        // Act
        this.componentUnderTest.contribute(builder);

        // Assert
        verify(this.environmentUtility).getOneActiveProfileOrDash();
        var builderDetailsAC = ArgumentCaptor.forClass(Map.class);
        verify(builder).withDetails(builderDetailsAC.capture());
        var details = builderDetailsAC.getValue();
        assertThat(details)
                .hasSize(2)
                .containsEntry("activeProfile", activeProfile)
                .containsEntry("maven", this.applicationFrameworkProperties.getMavenConfigs().asMavenDetails());
    }
}
