package io.tech1.framework.b2b.base.security.jwt.essence;

import io.tech1.framework.b2b.base.security.jwt.essense.AbstractEssenceConstructor;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.b2b.base.security.jwt.tests.stubbers.AbstractMockService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.DefaultUser;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.constants.TestsPropertiesConstants.SECURITY_JWT_CONFIGS;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomLongGreaterThanZero;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
class AbstractEssenceConstructorTest {

    private static Stream<Arguments> addDefaultUsersPresentTest() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(randomLongGreaterThanZero())
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
        AnyDbInvitationCodesRepository anyDbInvitationCodesRepository() {
            return mock(AnyDbInvitationCodesRepository.class);
        }

        @Bean
        AnyDbUsersRepository anyDbUsersRepository() {
            return mock(AnyDbUsersRepository.class);
        }

        @Bean
        AbstractMockService abstractMockService() {
            return mock(AbstractMockService.class);
        }

        @Bean("abstractEssenceConstructor")
        AbstractEssenceConstructor abstractEssenceConstructor() {
            return new AbstractEssenceConstructor(
                    this.anyDbInvitationCodesRepository(),
                    this.anyDbUsersRepository(),
                    this.applicationFrameworkProperties()
            ) {
                @Override
                public long saveDefaultUsers(List<DefaultUser> defaultUsers) {
                    abstractMockService().executeInheritedMethod();
                    return 0;
                }

                @Override
                public void saveInvitationCodes(DefaultUser defaultUser, List<SimpleGrantedAuthority> authorities) {
                    abstractMockService().executeInheritedMethod();
                }
            };
        }
    }

    // Repositories
    private final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    private final AnyDbUsersRepository anyDbUsersRepository;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;
    // Mock
    private final AbstractMockService abstractMockService;

    private final AbstractEssenceConstructor componentUnderTest;

    @Autowired
    AbstractEssenceConstructorTest(
            AnyDbInvitationCodesRepository anyDbInvitationCodesRepository,
            AnyDbUsersRepository anyDbUsersRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties,
            AbstractMockService abstractMockService,
            @Qualifier("abstractEssenceConstructor") AbstractEssenceConstructor componentUnderTest
    ) {
        this.anyDbInvitationCodesRepository = anyDbInvitationCodesRepository;
        this.anyDbUsersRepository = anyDbUsersRepository;
        this.applicationFrameworkProperties = applicationFrameworkProperties;
        this.abstractMockService = abstractMockService;
        this.componentUnderTest = componentUnderTest;
    }

    @BeforeEach
    void beforeEach() {
        reset(
                this.anyDbInvitationCodesRepository,
                this.anyDbUsersRepository,
                this.applicationFrameworkProperties,
                this.abstractMockService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.anyDbInvitationCodesRepository,
                this.anyDbUsersRepository,
                this.applicationFrameworkProperties,
                this.abstractMockService
        );
    }

    @ParameterizedTest
    @MethodSource("addDefaultUsersPresentTest")
    void addDefaultUsersPresentTest(long count) {
        // Arrange
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SECURITY_JWT_CONFIGS);
        when(this.anyDbUsersRepository.count()).thenReturn(count);

        // Act
        this.componentUnderTest.addDefaultUsers();

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.anyDbUsersRepository).count();
        if (count == 0) {
            verify(this.abstractMockService).executeInheritedMethod();
        }
    }

    @ParameterizedTest
    @MethodSource("addDefaultUsersPresentTest")
    void addDefaultUsersInvitationCodes(long count) {
        // Arrange
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SECURITY_JWT_CONFIGS);
        var username = Username.of("admin12");
        when(this.anyDbInvitationCodesRepository.countByOwner(username)).thenReturn(count);

        // Act
        this.componentUnderTest.addDefaultUsersInvitationCodes();

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.anyDbInvitationCodesRepository).countByOwner(username);
        if (count == 0) {
            verify(this.abstractMockService).executeInheritedMethod();
        }
    }
}
