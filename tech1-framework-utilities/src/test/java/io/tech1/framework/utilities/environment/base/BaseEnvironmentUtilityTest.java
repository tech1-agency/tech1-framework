package io.tech1.framework.utilities.environment.base;

import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseEnvironmentUtilityTest {

    private static Stream<Arguments> isProfilesTest() {
        return Stream.of(
                Arguments.of(new String[] { randomString() }, false, false, false),
                Arguments.of(new String[] { randomString(), randomString() }, false, false, false),
                Arguments.of(new String[] { "dev", randomString() }, true, false, false),
                Arguments.of(new String[] { randomString(), "dev" }, false, false, false),
                Arguments.of(new String[] { "stage", randomString() }, false, true, false),
                Arguments.of(new String[] { randomString(), "stage" }, false, false, false),
                Arguments.of(new String[] { "prod", randomString() }, false, false, true),
                Arguments.of(new String[] { randomString(), "prod" }, false, false, false)
        );
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        Environment environment() {
            return mock(Environment.class);
        }

        @Bean
        EnvironmentUtility environmentUtils() {
            return new BaseEnvironmentUtility(
                    this.environment()
            );
        }
    }

    private final Environment environment;

    private final EnvironmentUtility componentUnderTest;

    @BeforeEach
    public void before() {
        reset(
                this.environment
        );
    }

    @AfterEach
    public void after() {
        verifyNoMoreInteractions(
                this.environment
        );
    }

    @Test
    public void verifyProfilesConfigurationExceptionTest() {
        // Arrange
        when(this.environment.getActiveProfiles()).thenReturn(new String[] { "dev", "prod" });

        // Act
        var throwable = catchThrowable(this.componentUnderTest::verifyProfilesConfiguration);

        // Assert
        verify(this.environment).getActiveProfiles();
        assertThat(throwable.getMessage()).isEqualTo("Base Environment Utility contains ONLY one active profile");
    }

    @Test
    public void verifyProfilesConfigurationTest() {
        // Arrange
        when(this.environment.getActiveProfiles()).thenReturn(new String[] { "dev" });

        // Act
        this.componentUnderTest.verifyProfilesConfiguration();

        // Assert
        verify(this.environment).getActiveProfiles();
    }

    @Test
    public void getActiveProfileTest() {
        // Arrange
        var expectedProfile = "dev";
        when(this.environment.getActiveProfiles()).thenReturn(new String[] { expectedProfile });

        // Act
        String actualActiveProfile = this.componentUnderTest.getActiveProfile();

        // Assert
        verify(this.environment).getActiveProfiles();
        assertThat(actualActiveProfile).isEqualTo(expectedProfile);
    }

    @Test
    public void getActiveProfileSeveralActiveProfilesTest() {
        // Arrange
        var expectedProfile = "dev";
        when(this.environment.getActiveProfiles()).thenReturn(new String[] { expectedProfile, randomString() });

        // Act
        var actualActiveProfile = this.componentUnderTest.getActiveProfile();

        // Assert
        verify(this.environment).getActiveProfiles();
        AssertionsForClassTypes.assertThat(actualActiveProfile).isEqualTo(expectedProfile);
    }

    @ParameterizedTest
    @MethodSource("isProfilesTest")
    public void isProfilesTest(String[] profiles, boolean devExpected, boolean stageExpected, boolean prodExpected) {
        // Arrange
        when(this.environment.getActiveProfiles()).thenReturn(profiles);

        // Act
        var devActual = this.componentUnderTest.isDev();
        var stageActual = this.componentUnderTest.isStage();
        var prodActual = this.componentUnderTest.isProd();

        // Assert
        verify(this.environment, times(3)).getActiveProfiles();
        assertThat(devActual).isEqualTo(devExpected);
        assertThat(stageActual).isEqualTo(stageExpected);
        assertThat(prodActual).isEqualTo(prodExpected);
    }
}
