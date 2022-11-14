package io.tech1.framework.utilities.resources.actuator;

import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Map;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseInfoResourceTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        EnvironmentUtility environmentUtility() {
            return mock(EnvironmentUtility.class);
        }

        @Bean
        BaseInfoResource baseInfoResource() {
            return new BaseInfoResource(
                    this.environmentUtility()
            );
        }
    }

    private final EnvironmentUtility environmentUtility;

    private final BaseInfoResource componentUnderTest;

    @BeforeEach
    public void before() {
        reset(
                this.environmentUtility
        );
    }

    @AfterEach
    public void after() {
        verifyNoMoreInteractions(
                this.environmentUtility
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void verifyProfilesConfigurationExceptionTest() {
        // Arrange
        var activeProfile = randomString();
        var builder = mock(Info.Builder.class);
        when(this.environmentUtility.getActiveProfile()).thenReturn(activeProfile);

        // Act
        this.componentUnderTest.contribute(builder);

        // Assert
        verify(this.environmentUtility).getActiveProfile();
        var builderDetailsAC = ArgumentCaptor.forClass(Map.class);
        verify(builder).withDetails(builderDetailsAC.capture());
        var details = builderDetailsAC.getValue();
        assertThat(details.size()).isEqualTo(1);
        assertThat(details.get("activeProfile")).isEqualTo(activeProfile);
    }
}
