package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.TokensContextThrowerService;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims.valid;
import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.randomValidDefaultClaims;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoTokensContextThrowerServiceTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        JwtUserDetailsService jwtUserDetailsAssistant() {
            return mock(JwtUserDetailsService.class);
        }

        @Bean
        MongoUsersSessionsRepository userSessionRepository() {
            return mock(MongoUsersSessionsRepository.class);
        }

        @Bean
        SecurityJwtTokenUtils securityJwtTokenUtility() {
            return mock(SecurityJwtTokenUtils.class);
        }

        @Bean
        TokensContextThrowerService tokenContextThrowerService() {
            return new MongoTokensContextThrowerService(
                    this.jwtUserDetailsAssistant(),
                    this.userSessionRepository(),
                    this.securityJwtTokenUtility()
            );
        }
    }

    // Assistants
    private final JwtUserDetailsService jwtUserDetailsService;
    // Repositories
    private final MongoUsersSessionsRepository mongoUsersSessionsRepository;
    // Utilities
    private final SecurityJwtTokenUtils securityJwtTokenUtils;

    private final TokensContextThrowerService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.jwtUserDetailsService,
                this.mongoUsersSessionsRepository,
                this.securityJwtTokenUtils
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.jwtUserDetailsService,
                this.mongoUsersSessionsRepository,
                this.securityJwtTokenUtils
        );
    }

    @Test
    void verifyDbPresenceTest() throws CookieRefreshTokenDbNotFoundException {
        // Arrange
        var oldJwtRefreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = valid(oldJwtRefreshToken, randomValidDefaultClaims());
        var jwtUser = entity(JwtUser.class);
        when(this.jwtUserDetailsService.loadUserByUsername(validatedClaims.safeGetUsername().identifier())).thenReturn(jwtUser);
        when(this.mongoUsersSessionsRepository.isPresent(oldJwtRefreshToken)).thenReturn(true);

        // Act
        var dbUser = this.componentUnderTest.verifyDbPresenceOrThrow(validatedClaims, oldJwtRefreshToken);

        // Assert
        verify(this.jwtUserDetailsService).loadUserByUsername(validatedClaims.safeGetUsername().identifier());
        verify(this.mongoUsersSessionsRepository).isPresent(oldJwtRefreshToken);
        assertThat(dbUser).isEqualTo(jwtUser);
    }

    @Test
    void verifyDbPresenceThrowTest() {
        // Arrange
        var oldJwtRefreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = valid(oldJwtRefreshToken, randomValidDefaultClaims());
        var jwtUser = entity(JwtUser.class);
        when(this.jwtUserDetailsService.loadUserByUsername(validatedClaims.safeGetUsername().identifier())).thenReturn(jwtUser);
        when(this.mongoUsersSessionsRepository.isPresent(oldJwtRefreshToken)).thenReturn(false);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyDbPresenceOrThrow(validatedClaims, oldJwtRefreshToken));

        // Assert
        verify(this.jwtUserDetailsService).loadUserByUsername(validatedClaims.safeGetUsername().identifier());
        verify(this.mongoUsersSessionsRepository).isPresent(oldJwtRefreshToken);
        assertThat(throwable)
                .isInstanceOf(CookieRefreshTokenDbNotFoundException.class)
                .hasMessageContaining("JWT refresh token is not present in database. Username: " + validatedClaims.safeGetUsername());
    }
}
