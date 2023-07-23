package io.tech1.framework.b2b.mongodb.security.jwt.essence.base;

import io.tech1.framework.b2b.mongodb.security.jwt.constants.SecurityJwtConstants;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.essence.EssenceConstructor;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
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
class BaseEssenceConstructorTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        InvitationCodeRepository invitationCodeRepository() {
            return mock(InvitationCodeRepository.class);
        }

        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        EssenceConstructor essenceConstructor() {
            return new BaseEssenceConstructor(
                    this.invitationCodeRepository(),
                    this.userRepository(),
                    this.applicationFrameworkProperties
            );
        }
    }

    private final InvitationCodeRepository invitationCodeRepository;
    private final UserRepository userRepository;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final EssenceConstructor componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationCodeRepository,
                this.userRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationCodeRepository,
                this.userRepository
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
        when(this.userRepository.count()).thenReturn(randomLongGreaterThanZero());

        // Act
        this.componentUnderTest.addDefaultUsers();

        // Assert
        verify(this.userRepository).count();
        // No Actions
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    void addDefaultUsersTest() {
        // Arrange
        when(this.userRepository.count()).thenReturn(0L);
        var dbUserAC = ArgumentCaptor.forClass(List.class);
        long usersCount = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers().getUsers().size();

        // Act
        this.componentUnderTest.addDefaultUsers();

        // Assert
        verify(this.userRepository).count();
        verify(this.userRepository).saveAll(dbUserAC.capture());
        assertThat(dbUserAC.getValue().size()).isEqualTo(usersCount);
    }

    @Test
    void addDefaultUsersInvitationCodesAlreadyPresentTest() {
        // Arrange
        var username = this.getDefaultUserUsername();
        var invitationCodes = list345(DbInvitationCode.class);
        when(this.invitationCodeRepository.findByOwner(eq(username))).thenReturn(invitationCodes);

        // Act
        this.componentUnderTest.addDefaultUsersInvitationCodes();

        // Assert
        verify(this.invitationCodeRepository).findByOwner(eq(username));
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    void addDefaultUsersInvitationCodesNotPresentTest() {
        // Arrange
        var username = this.getDefaultUserUsername();
        when(this.invitationCodeRepository.findByOwner(eq(username))).thenReturn(emptyList());

        // Act
        this.componentUnderTest.addDefaultUsersInvitationCodes();

        // Assert
        verify(this.invitationCodeRepository).findByOwner(eq(username));
        var invitationCodesAC = ArgumentCaptor.forClass(List.class);
        verify(this.invitationCodeRepository).saveAll(invitationCodesAC.capture());
        List<DbInvitationCode> invitationCodes = invitationCodesAC.getValue();
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
            assertThat(invitationCode.getValue().length()).isEqualTo(SecurityJwtConstants.DEFAULT_INVITATION_CODE_LENGTH);
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
