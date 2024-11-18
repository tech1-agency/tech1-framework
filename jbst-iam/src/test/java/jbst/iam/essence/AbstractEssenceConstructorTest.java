package jbst.iam.essence;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.domain.properties.base.DefaultUser;
import jbst.foundation.domain.properties.configs.SecurityJwtConfigs;
import jbst.iam.repositories.InvitationCodesRepository;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.tests.stubbers.AbstractMockService;
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
import java.util.Set;
import java.util.stream.Stream;

import static jbst.foundation.utilities.random.RandomUtility.randomLongGreaterThanZero;
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
        JbstProperties jbstProperties() {
            return mock(JbstProperties.class);
        }

        @Bean
        AbstractEssenceConstructor essenceConstructor() {
            var essenceConstructor = mock(AbstractEssenceConstructor.class);
            setField(essenceConstructor, "jbstProperties", this.jbstProperties());
            return essenceConstructor;
        }

        @Bean
        InvitationCodesRepository invitationCodesRepository() {
            return mock(InvitationCodesRepository.class);
        }

        @Bean
        UsersRepository usersRepository() {
            return mock(UsersRepository.class);
        }

        @Bean
        AbstractMockService abstractMockService() {
            return mock(AbstractMockService.class);
        }

        @Bean("abstractEssenceConstructor")
        AbstractEssenceConstructor abstractEssenceConstructor() {
            return new AbstractEssenceConstructor(
                    this.invitationCodesRepository(),
                    this.usersRepository(),
                    this.jbstProperties()
            ) {
                @Override
                public long saveDefaultUsers(List<DefaultUser> defaultUsers) {
                    abstractMockService().executeInheritedMethod();
                    return 0;
                }

                @Override
                public void saveInvitationCodes(DefaultUser defaultUser, Set<SimpleGrantedAuthority> authorities) {
                    abstractMockService().executeInheritedMethod();
                }
            };
        }
    }

    // Repositories
    private final InvitationCodesRepository invitationCodesRepository;
    private final UsersRepository usersRepository;
    // Properties
    private final JbstProperties jbstProperties;
    // Mock
    private final AbstractMockService abstractMockService;

    private final AbstractEssenceConstructor componentUnderTest;

    @Autowired
    AbstractEssenceConstructorTest(
            InvitationCodesRepository invitationCodesRepository,
            UsersRepository usersRepository,
            JbstProperties jbstProperties,
            AbstractMockService abstractMockService,
            @Qualifier("abstractEssenceConstructor") AbstractEssenceConstructor componentUnderTest
    ) {
        this.invitationCodesRepository = invitationCodesRepository;
        this.usersRepository = usersRepository;
        this.jbstProperties = jbstProperties;
        this.abstractMockService = abstractMockService;
        this.componentUnderTest = componentUnderTest;
    }

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationCodesRepository,
                this.usersRepository,
                this.jbstProperties,
                this.abstractMockService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationCodesRepository,
                this.usersRepository,
                this.jbstProperties,
                this.abstractMockService
        );
    }

    @ParameterizedTest
    @MethodSource("addDefaultUsersPresentTest")
    void addDefaultUsersPresentTest(long count) {
        // Arrange
        when(this.jbstProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.hardcoded());
        when(this.usersRepository.count()).thenReturn(count);

        // Act
        this.componentUnderTest.addDefaultUsers();

        // Assert
        verify(this.jbstProperties).getSecurityJwtConfigs();
        verify(this.usersRepository).count();
        if (count == 0) {
            verify(this.abstractMockService).executeInheritedMethod();
        }
    }

    @ParameterizedTest
    @MethodSource("addDefaultUsersPresentTest")
    void addDefaultUsersInvitationCodes(long count) {
        // Arrange
        when(this.jbstProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.hardcoded());
        var username = Username.of("admin12");
        when(this.invitationCodesRepository.countByOwner(username)).thenReturn(count);

        // Act
        this.componentUnderTest.addDefaultUsersInvitationCodes();

        // Assert
        verify(this.jbstProperties).getSecurityJwtConfigs();
        verify(this.invitationCodesRepository).countByOwner(username);
        if (count == 0) {
            verify(this.abstractMockService).executeInheritedMethod();
        }
    }
}
