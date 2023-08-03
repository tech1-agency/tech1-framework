package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.exceptions.cookie.*;
import io.tech1.framework.domain.tuples.TuplePresence;
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

import static io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims.invalid;
import static io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims.valid;
import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.expiredClaims;
import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.validClaims;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractTokensContextThrowerServiceTest {

    private static Stream<Arguments> verifyAccessTokenExpirationOrThrow() {
        return Stream.of(
                Arguments.of(valid(entity(JwtAccessToken.class), validClaims()), false),
                Arguments.of(valid(entity(JwtRefreshToken.class), validClaims()), false),
                Arguments.of(valid(entity(JwtAccessToken.class), expiredClaims()), true),
                Arguments.of(valid(entity(JwtRefreshToken.class), expiredClaims()), false)
        );
    }

    private static Stream<Arguments> verifyRefreshTokenExpirationOrThrowTest() {
        return Stream.of(
                Arguments.of(valid(entity(JwtAccessToken.class), validClaims()), false),
                Arguments.of(valid(entity(JwtRefreshToken.class), validClaims()), false),
                Arguments.of(valid(entity(JwtAccessToken.class), expiredClaims()), false),
                Arguments.of(valid(entity(JwtRefreshToken.class), expiredClaims()), true)
        );
    }

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        JwtUserDetailsService jwtUserDetailsService() {
            return mock(JwtUserDetailsService.class);
        }

        @Bean
        AnyDbUsersSessionsRepository usersSessionsRepository() {
            return mock(AnyDbUsersSessionsRepository.class);
        }

        @Bean
        SecurityJwtTokenUtils securityJwtTokenUtils() {
            return mock(SecurityJwtTokenUtils.class);
        }

        @Bean
        AbstractTokensContextThrowerService abstractTokensContextThrowerService() {
            return new AbstractTokensContextThrowerService(
                    this.jwtUserDetailsService(),
                    this.usersSessionsRepository(),
                    this.securityJwtTokenUtils()
            ) {};
        }
    }

    // Assistants
    private final JwtUserDetailsService jwtUserDetailsService;
    // Repositories
    private final AnyDbUsersSessionsRepository usersSessionsRepository;
    // Utilities
    private final SecurityJwtTokenUtils securityJwtTokenUtils;

    private final AbstractTokensContextThrowerService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtTokenUtils
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtTokenUtils
        );
    }

    @Test
    void verifyValidityAccessTokenTest() throws CookieAccessTokenInvalidException {
        // Arrange
        var accessToken = entity(JwtAccessToken.class);
        var validatedClaims = valid(accessToken, validClaims());
        when(this.securityJwtTokenUtils.validate(accessToken)).thenReturn(validatedClaims);

        // Act
        this.componentUnderTest.verifyValidityOrThrow(accessToken);

        // Assert
        verify(this.securityJwtTokenUtils).validate(accessToken);
    }

    @Test
    void verifyValidityAccessTokenThrowTest() {
        // Arrange
        var jwtAccessToken = entity(JwtAccessToken.class);
        var validatedClaims = invalid(jwtAccessToken);
        when(this.securityJwtTokenUtils.validate(jwtAccessToken)).thenReturn(validatedClaims);

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
        var refreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = valid(refreshToken, validClaims());
        when(this.securityJwtTokenUtils.validate(refreshToken)).thenReturn(validatedClaims);

        // Act
        this.componentUnderTest.verifyValidityOrThrow(refreshToken);

        // Assert
        verify(this.securityJwtTokenUtils).validate(refreshToken);
    }

    @Test
    void verifyValidityRefreshTokenThrowTest() {
        // Arrange
        var refreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = invalid(refreshToken);
        when(this.securityJwtTokenUtils.validate(refreshToken)).thenReturn(validatedClaims);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyValidityOrThrow(refreshToken));

        // Assert
        verify(this.securityJwtTokenUtils).validate(refreshToken);
        assertThat(throwable)
                .isInstanceOf(CookieRefreshTokenInvalidException.class)
                .hasMessageContaining("JWT refresh token is invalid");
    }

    @ParameterizedTest
    @MethodSource("verifyAccessTokenExpirationOrThrow")
    void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims, boolean throwableFlag) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyAccessTokenExpirationOrThrow(validatedClaims));

        // Assert
        if (throwableFlag) {
            assertThat(throwable)
                    .isInstanceOf(CookieAccessTokenExpiredException.class)
                    .hasMessageContaining("JWT access token is expired. Username: " + validatedClaims.username());
        }
    }

    @ParameterizedTest
    @MethodSource("verifyRefreshTokenExpirationOrThrowTest")
    void verifyRefreshTokenExpirationOrThrowTest(JwtTokenValidatedClaims validatedClaims, boolean throwableFlag) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyRefreshTokenExpirationOrThrow(validatedClaims));

        // Assert
        if (throwableFlag) {
            assertThat(throwable)
                    .isInstanceOf(CookieRefreshTokenExpiredException.class)
                    .hasMessageContaining("JWT refresh token is expired. Username: " + validatedClaims.username());
        }
    }

    @Test
    void verifyAccessTokenDbPresenceTest() throws CookieAccessTokenDbNotFoundException {
        // Arrange
        var accessToken = entity(JwtAccessToken.class);
        var validatedClaims = valid(accessToken, validClaims());
        when(this.usersSessionsRepository.isPresent(accessToken)).thenReturn(TuplePresence.present(entity(AnyDbUserSession.class)));

        // Act
        this.componentUnderTest.verifyDbPresenceOrThrow(accessToken, validatedClaims);

        // Assert
        verify(this.usersSessionsRepository).isPresent(accessToken);
    }

    @Test
    void verifyAccessTokenDbPresenceThrowTest() {
        // Arrange
        var accessToken = entity(JwtAccessToken.class);
        var validatedClaims = valid(accessToken, validClaims());
        when(this.usersSessionsRepository.isPresent(accessToken)).thenReturn(TuplePresence.absent());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyDbPresenceOrThrow(accessToken, validatedClaims));

        // Assert
        verify(this.usersSessionsRepository).isPresent(accessToken);
        assertThat(throwable)
                .isInstanceOf(CookieAccessTokenDbNotFoundException.class)
                .hasMessageContaining("JWT access token is not present in database. Username: " + validatedClaims.username());
    }

    @Test
    void verifyRefreshTokenDbPresenceTest() throws CookieRefreshTokenDbNotFoundException {
        // Arrange
        var refreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = valid(refreshToken, validClaims());
        var user = entity(JwtUser.class);
        var session = entity(AnyDbUserSession.class);
        when(this.jwtUserDetailsService.loadUserByUsername(validatedClaims.username().identifier())).thenReturn(user);
        when(this.usersSessionsRepository.isPresent(refreshToken)).thenReturn(TuplePresence.present(session));

        // Act
        var actual = this.componentUnderTest.verifyDbPresenceOrThrow(refreshToken, validatedClaims);

        // Assert
        verify(this.jwtUserDetailsService).loadUserByUsername(validatedClaims.username().identifier());
        verify(this.usersSessionsRepository).isPresent(refreshToken);
        assertThat(actual.a()).isEqualTo(user);
        assertThat(actual.b()).isEqualTo(session);
    }

    @Test
    void verifyRefreshTokenDbPresenceThrowTest() {
        // Arrange
        var refreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = valid(refreshToken, validClaims());
        when(this.usersSessionsRepository.isPresent(refreshToken)).thenReturn(TuplePresence.absent());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyDbPresenceOrThrow(refreshToken, validatedClaims));

        // Assert
        verify(this.usersSessionsRepository).isPresent(refreshToken);
        assertThat(throwable)
                .isInstanceOf(CookieRefreshTokenDbNotFoundException.class)
                .hasMessageContaining("JWT refresh token is not present in database. Username: " + validatedClaims.username());
    }
}
