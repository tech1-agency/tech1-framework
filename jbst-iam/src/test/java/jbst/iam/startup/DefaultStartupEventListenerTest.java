package jbst.iam.startup;

import jbst.iam.essence.AbstractEssenceConstructor;
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
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.domain.properties.base.Authority;
import tech1.framework.foundation.domain.properties.base.DefaultUsers;
import tech1.framework.foundation.domain.properties.base.InvitationCodes;
import tech1.framework.foundation.domain.properties.configs.SecurityJwtConfigs;
import tech1.framework.foundation.domain.properties.configs.security.jwt.AuthoritiesConfigs;
import tech1.framework.foundation.domain.properties.configs.security.jwt.EssenceConfigs;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class DefaultStartupEventListenerTest {

    private static Stream<Arguments> onStartupTest() {
        return Stream.of(
                Arguments.of(true, true),
                Arguments.of(true, false),
                Arguments.of(false, true),
                Arguments.of(false, false)
        );
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        AbstractEssenceConstructor essenceConstructor() {
            var essenceConstructor = mock(AbstractEssenceConstructor.class);
            setField(essenceConstructor, "applicationFrameworkProperties", this.applicationFrameworkProperties());
            return essenceConstructor;
        }

        @Bean
        BaseStartupEventListener baseStartupEventListener() {
            return new DefaultStartupEventListener(
                    this.essenceConstructor(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final AbstractEssenceConstructor essenceConstructor;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final DefaultStartupEventListener componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.essenceConstructor,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.essenceConstructor,
                this.applicationFrameworkProperties
        );
    }

    @ParameterizedTest
    @MethodSource("onStartupTest")
    void onStartupTest(boolean isDefaultUsersEnabled, boolean isInvitationCodesEnabled) {
        // Arrange
        SecurityJwtConfigs securityJwtConfigs = new SecurityJwtConfigs(
                new AuthoritiesConfigs(
                        "tech1",
                        Set.of(
                                new Authority("admin"),
                                new Authority("user")
                        )
                ),
                null,
                new EssenceConfigs(
                        new DefaultUsers(
                                isDefaultUsersEnabled,
                                new ArrayList<>()
                        ),
                        new InvitationCodes(
                                isInvitationCodesEnabled
                        )
                ),
                null,
                null,
                null,
                null,
                null
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);

        // Act
        this.componentUnderTest.onStartup();

        // Assert
        verify(this.applicationFrameworkProperties, times(2)).getSecurityJwtConfigs();
        if (isDefaultUsersEnabled) {
            verify(this.essenceConstructor).addDefaultUsers();
        }
        if (isInvitationCodesEnabled) {
            verify(this.essenceConstructor).addDefaultUsersInvitationCodes();
        }
        reset(
                this.essenceConstructor,
                this.applicationFrameworkProperties
        );
    }
}
