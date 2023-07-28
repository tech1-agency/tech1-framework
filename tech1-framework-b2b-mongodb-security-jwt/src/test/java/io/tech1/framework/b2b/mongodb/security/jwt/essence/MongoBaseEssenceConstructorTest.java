package io.tech1.framework.b2b.mongodb.security.jwt.essence;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.essense.EssenceConstructor;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserRepository;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static io.tech1.framework.domain.base.AbstractAuthority.INVITATION_CODE_READ;
import static io.tech1.framework.domain.base.AbstractAuthority.INVITATION_CODE_WRITE;
import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomLongGreaterThanZero;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoBaseEssenceConstructorTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        MongoInvitationCodesRepository invitationCodeRepository() {
            return mock(MongoInvitationCodesRepository.class);
        }

        @Bean
        MongoUserRepository userRepository() {
            return mock(MongoUserRepository.class);
        }

        @Bean
        EssenceConstructor essenceConstructor() {
            return new MongoBaseEssenceConstructor(
                    this.invitationCodeRepository(),
                    this.userRepository(),
                    this.applicationFrameworkProperties
            );
        }
    }

    private final MongoInvitationCodesRepository mongoInvitationCodesRepository;
    private final MongoUserRepository mongoUserRepository;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final EssenceConstructor componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.mongoInvitationCodesRepository,
                this.mongoUserRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.mongoInvitationCodesRepository,
                this.mongoUserRepository
        );
    }

    @Test
    void isDefaultUsersEnabledTest() {
        // Arrange
        var expectedFlag = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers().isEnabled();

        // Act
        var actualFlag = this.componentUnderTest.isDefaultUsersEnabled();

        // Assert
        assertThat(actualFlag).isEqualTo(expectedFlag);
    }

    @Test
    void isInvitationCodesEnabledTest() {
        // Arrange
        var expectedFlag = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getInvitationCodes().isEnabled();

        // Act
        var actualFlag = this.componentUnderTest.isInvitationCodesEnabled();

        // Assert
        assertThat(actualFlag).isEqualTo(expectedFlag);
    }

    @Test
    void addDefaultUsersNoActionsTest() {
        // Arrange
        when(this.mongoUserRepository.count()).thenReturn(randomLongGreaterThanZero());

        // Act
        this.componentUnderTest.addDefaultUsers();

        // Assert
        verify(this.mongoUserRepository).count();
        // No Actions
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    void addDefaultUsersTest() {
        // Arrange
        when(this.mongoUserRepository.count()).thenReturn(0L);
        var dbUserAC = ArgumentCaptor.forClass(List.class);
        int usersCount = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers().getUsers().size();

        // Act
        this.componentUnderTest.addDefaultUsers();

        // Assert
        verify(this.mongoUserRepository).count();
        verify(this.mongoUserRepository).saveAll(dbUserAC.capture());
        assertThat(dbUserAC.getValue()).hasSize(usersCount);
    }

    @Test
    void addDefaultUsersInvitationCodesAlreadyPresentTest() {
        // Arrange
        var username = this.getDefaultUserUsername();
        var invitationCodes = list345(MongoDbInvitationCode.class);
        when(this.mongoInvitationCodesRepository.findByOwner(username)).thenReturn(invitationCodes);

        // Act
        this.componentUnderTest.addDefaultUsersInvitationCodes();

        // Assert
        verify(this.mongoInvitationCodesRepository).findByOwner(username);
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    void addDefaultUsersInvitationCodesNotPresentTest() {
        // Arrange
        var username = this.getDefaultUserUsername();
        when(this.mongoInvitationCodesRepository.findByOwner(username)).thenReturn(emptyList());

        // Act
        this.componentUnderTest.addDefaultUsersInvitationCodes();

        // Assert
        verify(this.mongoInvitationCodesRepository).findByOwner(username);
        var invitationCodesAC = ArgumentCaptor.forClass(List.class);
        verify(this.mongoInvitationCodesRepository).saveAll(invitationCodesAC.capture());
        List<MongoDbInvitationCode> invitationCodes = invitationCodesAC.getValue();
        assertThat(invitationCodes).hasSize(10);
        invitationCodes.forEach(invitationCode -> {
            assertThat(invitationCode.getOwner()).isEqualTo(username);
            assertThat(invitationCode.getAuthorities()).hasSize(4);
            assertThat(invitationCode.getAuthorities()).containsExactlyInAnyOrder(
                    new SimpleGrantedAuthority("admin"),
                    new SimpleGrantedAuthority("user"),
                    new SimpleGrantedAuthority(INVITATION_CODE_READ),
                    new SimpleGrantedAuthority(INVITATION_CODE_WRITE)
            );
            assertThat(invitationCode.getValue()).hasSize(40);
        });
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private Username getDefaultUserUsername() {
        var defaultUsers = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers();
        return defaultUsers.getUsers().get(0).getUsername();
    }
}
