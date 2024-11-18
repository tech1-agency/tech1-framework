package jbst.iam.services.abstracts;

import jbst.iam.domain.dto.requests.RequestNewInvitationParams;
import jbst.iam.domain.dto.responses.ResponseInvitation;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.repositories.InvitationsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.configurations.ConfigurationPropertiesJbstHardcoded;

import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseInvitationsServiceTest {

    @Configuration
    @Import({
            ConfigurationPropertiesJbstHardcoded.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final JbstProperties jbstProperties;

        @Bean
        InvitationsRepository invitationCodesRepository() {
            return mock(InvitationsRepository.class);
        }

        @Bean
        AbstractBaseInvitationsService abstractBaseInvitationCodesService() {
            return new AbstractBaseInvitationsService(
                    this.invitationCodesRepository(),
                    this.jbstProperties
            ) {};
        }
    }

    private final InvitationsRepository invitationsRepository;
    private final JbstProperties jbstProperties;

    private final AbstractBaseInvitationsService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationsRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationsRepository
        );
    }

    @Test
    void findByOwnerTest() {
        // Arrange
        var owner = Username.random();

        var invitationCode1 = ResponseInvitation.random(owner, Username.of("user2"));
        var invitationCode2 = ResponseInvitation.random(owner, Username.of("user1"));
        var invitationCode3 = ResponseInvitation.random(owner);
        var invitationCode4 = ResponseInvitation.random(owner);
        var invitationCode5 = ResponseInvitation.random(owner, Username.of("user5"));
        var invitationCode6 = ResponseInvitation.random(owner);

        var invitationCodes = asList(invitationCode1, invitationCode2, invitationCode3, invitationCode4, invitationCode5, invitationCode6);
        when(this.invitationsRepository.findResponseCodesByOwner(owner)).thenReturn(invitationCodes);

        // Act
        var responseInvitationCodes = this.componentUnderTest.findByOwner(owner);

        // Assert
        verify(this.invitationsRepository).findResponseCodesByOwner(owner);
        assertThat(responseInvitationCodes.invitations().stream()
                        .limit(3)
                        .map(ResponseInvitation::value)
                        .collect(Collectors.toSet())
        ).containsExactlyInAnyOrder(
                invitationCode3.value(),
                invitationCode4.value(),
                invitationCode6.value()
        );
        assertThat(responseInvitationCodes.invitations().stream()
                .skip(3)
                .map(ResponseInvitation::value)
                .collect(Collectors.toSet())
        ).containsExactlyInAnyOrder(
                invitationCode1.value(),
                invitationCode2.value(),
                invitationCode5.value()
        );
        assertThat(responseInvitationCodes.authorities()).isEqualTo(this.jbstProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities());
    }

    @Test
    void saveTest() {
        // Arrange
        var username = Username.random();
        var request = RequestNewInvitationParams.random();

        // Act
        this.componentUnderTest.save(username, request);

        // Assert
        verify(this.invitationsRepository).saveAs(username, request);
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        var invitationCodeId = InvitationId.random();

        // Act
        this.componentUnderTest.deleteById(invitationCodeId);

        // Assert
        verify(this.invitationsRepository).delete(invitationCodeId);
    }
}
