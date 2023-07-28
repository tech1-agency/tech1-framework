package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.TokenContextThrowerService;
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
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtRandomUtility.randomValidDefaultClaims;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoTokenContextThrowerServiceTest {

    private static Stream<Arguments> verifyAccessTokenExpirationOrThrow() {
        return Stream.of(
                Arguments.of(valid(entity(JwtAccessToken.class), randomValidDefaultClaims()), false, false),
                Arguments.of(valid(entity(JwtRefreshToken.class), randomValidDefaultClaims()), false, false),
                Arguments.of(valid(entity(JwtAccessToken.class), randomValidDefaultClaims()), true, true),
                Arguments.of(valid(entity(JwtRefreshToken.class), randomValidDefaultClaims()), true, false)
        );
    }

    private static Stream<Arguments> verifyRefreshTokenExpirationOrThrowTest() {
        return Stream.of(
                Arguments.of(valid(entity(JwtAccessToken.class), randomValidDefaultClaims()), false, false),
                Arguments.of(valid(entity(JwtRefreshToken.class), randomValidDefaultClaims()), false, false),
                Arguments.of(valid(entity(JwtAccessToken.class), randomValidDefaultClaims()), true, false),
                Arguments.of(valid(entity(JwtRefreshToken.class), randomValidDefaultClaims()), true, true)
        );
    }

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
        TokenContextThrowerService tokenContextThrowerService() {
            return new MongoTokenContextThrowerService(
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

    private final TokenContextThrowerService componentUnderTest;

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
    void verifyValidityAccessTokenTest() throws CookieAccessTokenInvalidException {
        // Arrange
        var jwtAccessToken = entity(JwtAccessToken.class);
        when(this.securityJwtTokenUtils.validate(jwtAccessToken)).thenReturn(valid(jwtAccessToken, randomValidDefaultClaims()));

        // Act
        this.componentUnderTest.verifyValidityOrThrow(jwtAccessToken);

        // Assert
        verify(this.securityJwtTokenUtils).validate(jwtAccessToken);
    }

    @Test
    void verifyValidityAccessTokenThrowTest() {
        // Arrange
        var jwtAccessToken = entity(JwtAccessToken.class);
        when(this.securityJwtTokenUtils.validate(jwtAccessToken)).thenReturn(JwtTokenValidatedClaims.invalid(jwtAccessToken));

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyValidityOrThrow(jwtAccessToken));

        // Assert
        verify(this.securityJwtTokenUtils).validate(jwtAccessToken);
        assertThat(throwable)
                .isInstanceOf(CookieAccessTokenInvalidException.class)
                .hasMessageContaining("JWT access token is invalid");
    }

    @Test
    void verifyValidityRefreshTokenTest() throws CookieRefreshTokenInvalidException {
        // Arrange
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        when(this.securityJwtTokenUtils.validate(jwtRefreshToken)).thenReturn(valid(jwtRefreshToken, randomValidDefaultClaims()));

        // Act
        this.componentUnderTest.verifyValidityOrThrow(jwtRefreshToken);

        // Assert
        verify(this.securityJwtTokenUtils).validate(jwtRefreshToken);
    }

    @Test
    void verifyValidityRefreshTokenThrowTest() {
        // Arrange
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        when(this.securityJwtTokenUtils.validate(jwtRefreshToken)).thenReturn(JwtTokenValidatedClaims.invalid(jwtRefreshToken));

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyValidityOrThrow(jwtRefreshToken));

        // Assert
        verify(this.securityJwtTokenUtils).validate(jwtRefreshToken);
        assertThat(throwable)
                .isInstanceOf(CookieRefreshTokenInvalidException.class)
                .hasMessageContaining("JWT refresh token is invalid");
    }

    @ParameterizedTest
    @MethodSource("verifyAccessTokenExpirationOrThrow")
    void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims, boolean expiredFlag, boolean throwableFlag) {
        // Arrange
        when(this.securityJwtTokenUtils.isExpired(validatedClaims)).thenReturn(expiredFlag);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyAccessTokenExpirationOrThrow(validatedClaims));

        // Assert
        if (throwableFlag) {
            assertThat(throwable)
                    .isInstanceOf(CookieAccessTokenExpiredException.class)
                    .hasMessageContaining("JWT access token is expired. Username: " + validatedClaims.safeGetUsername());
        } else {
            verify(this.securityJwtTokenUtils).isExpired(validatedClaims);
        }
        reset(
                this.securityJwtTokenUtils
        );
    }

    @ParameterizedTest
    @MethodSource("verifyRefreshTokenExpirationOrThrowTest")
    void verifyRefreshTokenExpirationOrThrowTest(JwtTokenValidatedClaims validatedClaims, boolean expiredFlag, boolean throwableFlag) {
        // Arrange
        when(this.securityJwtTokenUtils.isExpired(validatedClaims)).thenReturn(expiredFlag);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyRefreshTokenExpirationOrThrow(validatedClaims));

        // Assert
        if (throwableFlag) {
            assertThat(throwable)
                    .isInstanceOf(CookieRefreshTokenExpiredException.class)
                    .hasMessageContaining("JWT refresh token is expired. Username: " + validatedClaims.safeGetUsername());
        } else {
            verify(this.securityJwtTokenUtils).isExpired(validatedClaims);
        }
        reset(
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
