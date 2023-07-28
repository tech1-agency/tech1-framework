package io.tech1.framework.b2b.base.security.jwt.startup;

import io.tech1.framework.b2b.base.security.jwt.essense.EssenceConstructor;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class DefaultStartupEventListenerTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        EssenceConstructor essenceConstructor() {
            return mock(EssenceConstructor.class);
        }

        @Bean
        EnvironmentUtility environmentUtility() {
            return mock(EnvironmentUtility.class);
        }

        @Bean
        BaseStartupEventListener baseStartupEventListener() {
            return new DefaultStartupEventListener(
                    this.essenceConstructor(),
                    this.environmentUtility()
            );
        }
    }

    private final EssenceConstructor essenceConstructor;
    private final EnvironmentUtility environmentUtility;

    private final DefaultStartupEventListener componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.essenceConstructor,
                this.environmentUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.essenceConstructor,
                this.environmentUtility
        );
    }

    private static Stream<Arguments> onStartupTest() {
        return Stream.of(
                Arguments.of(true, true),
                Arguments.of(true, false),
                Arguments.of(false, true),
                Arguments.of(false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("onStartupTest")
    void onStartupTest(boolean isDefaultUsersEnabled, boolean isInvitationCodesEnabled) {
        // Arrange
        when(this.essenceConstructor.isDefaultUsersEnabled()).thenReturn(isDefaultUsersEnabled);
        when(this.essenceConstructor.isInvitationCodesEnabled()).thenReturn(isInvitationCodesEnabled);

        // Act
        this.componentUnderTest.onStartup();

        // Assert
        verify(this.environmentUtility).verifyProfilesConfiguration();
        verify(this.essenceConstructor).isDefaultUsersEnabled();
        verify(this.essenceConstructor).isInvitationCodesEnabled();
        if (isDefaultUsersEnabled) {
            verify(this.essenceConstructor).addDefaultUsers();
        }
        if (isInvitationCodesEnabled) {
            verify(this.essenceConstructor).addDefaultUsersInvitationCodes();
        }
        reset(
                this.essenceConstructor,
                this.environmentUtility
        );
    }
}
