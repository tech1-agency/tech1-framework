package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.InvitationCodeService;
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

import java.util.HashSet;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class InvitationCodeServiceImplTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {

        // Properties
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        InvitationCodeRepository invitationCodeRepository() {
            return mock(InvitationCodeRepository.class);
        }

        @Bean
        InvitationCodeService invitationCodeService() {
            return new InvitationCodeServiceImpl(
                    this.invitationCodeRepository(),
                    this.applicationFrameworkProperties
            );
        }
    }

    private final InvitationCodeRepository invitationCodeRepository;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final InvitationCodeService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationCodeRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationCodeRepository
        );
    }

    @Test
    void findByOwnerTest() {
        // Arrange
        var owner = randomUsername();
        var authorities = singletonList(new SimpleGrantedAuthority("admin"));
        var invitationCode1 = new DbInvitationCode(owner, authorities);
        var invitationCode2 = new DbInvitationCode(owner, authorities);
        var invitationCode3 = new DbInvitationCode(owner, authorities);
        var invitationCode4 = new DbInvitationCode(owner, authorities);
        var invitationCode5 = new DbInvitationCode(owner, authorities);
        var invitationCode6 = new DbInvitationCode(owner, authorities);

        invitationCode2.editInvited(Username.of("user1"));
        invitationCode1.editInvited(Username.of("user2"));
        invitationCode5.editInvited(Username.of("user5"));

        var invitationCodes = asList(invitationCode1, invitationCode2, invitationCode3, invitationCode4, invitationCode5, invitationCode6);
        when(this.invitationCodeRepository.findByOwner(owner)).thenReturn(invitationCodes);

        // Act
        var responseInvitationCodes = this.componentUnderTest.findByOwner(owner);

        // Assert
        verify(this.invitationCodeRepository).findByOwner(owner);
        assertThat(responseInvitationCodes.invitationCodes()).isEqualTo(invitationCodes);
        assertThat(responseInvitationCodes.invitationCodes().get(0)).isEqualTo(invitationCode3);
        assertThat(responseInvitationCodes.invitationCodes().get(1)).isEqualTo(invitationCode4);
        assertThat(responseInvitationCodes.invitationCodes().get(2)).isEqualTo(invitationCode6);
        assertThat(responseInvitationCodes.invitationCodes().get(3)).isEqualTo(invitationCode2);
        assertThat(responseInvitationCodes.invitationCodes().get(4)).isEqualTo(invitationCode1);
        assertThat(responseInvitationCodes.invitationCodes().get(5)).isEqualTo(invitationCode5);
        assertThat(responseInvitationCodes.authorities()).isEqualTo(this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities());
    }

    @Test
    void saveTest() {
        // Arrange
        var dbUser = entity(DbUser.class);
        var requestNewInvitationCodeParams = new RequestNewInvitationCodeParams(new HashSet<>(randomStringsAsList(3)));
        var dbInvitationCodeAC = ArgumentCaptor.forClass(DbInvitationCode.class);

        // Act
        this.componentUnderTest.save(requestNewInvitationCodeParams, dbUser.getUsername());

        // Assert
        verify(this.invitationCodeRepository).save(dbInvitationCodeAC.capture());
        assertThat(dbInvitationCodeAC.getValue().getAuthorities()).isEqualTo(requestNewInvitationCodeParams.authorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        assertThat(dbInvitationCodeAC.getValue().getOwner()).isEqualTo(dbUser.getUsername());
        assertThat(dbInvitationCodeAC.getValue().getValue()).hasSize(40);
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        var invitationCodeId = randomString();

        // Act
        this.componentUnderTest.deleteById(invitationCodeId);

        // Assert
        verify(this.invitationCodeRepository).deleteById(invitationCodeId);
    }
}
