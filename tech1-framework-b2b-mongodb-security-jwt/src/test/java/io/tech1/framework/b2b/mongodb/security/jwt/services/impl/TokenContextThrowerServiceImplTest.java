package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails.JwtUserDetailsAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenContextThrowerService;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
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

import static io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtTokenValidatedClaims.valid;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtRandomUtility.randomValidDefaultClaims;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenContextThrowerServiceImplTest {

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
        JwtUserDetailsAssistant jwtUserDetailsAssistant() {
            return mock(JwtUserDetailsAssistant.class);
        }

        @Bean
        UserSessionRepository userSessionRepository() {
            return mock(UserSessionRepository.class);
        }

        @Bean
        SecurityJwtTokenUtility securityJwtTokenUtility() {
            return mock(SecurityJwtTokenUtility.class);
        }

        @Bean
        TokenContextThrowerService tokenContextThrowerService() {
            return new TokenContextThrowerServiceImpl(
                    this.jwtUserDetailsAssistant(),
                    this.userSessionRepository(),
                    this.securityJwtTokenUtility()
            );
        }
    }

    // Assistants
    private final JwtUserDetailsAssistant jwtUserDetailsAssistant;
    // Repositories
    private final UserSessionRepository userSessionRepository;
    // Utilities
    private final SecurityJwtTokenUtility securityJwtTokenUtility;

    private final TokenContextThrowerService componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.jwtUserDetailsAssistant,
                this.userSessionRepository,
                this.securityJwtTokenUtility
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.jwtUserDetailsAssistant,
                this.userSessionRepository,
                this.securityJwtTokenUtility
        );
    }

    @Test
    public void verifyValidityAccessTokenTest() throws CookieAccessTokenInvalidException {
        // Arrange
        var jwtAccessToken = entity(JwtAccessToken.class);
        when(this.securityJwtTokenUtility.validate(jwtAccessToken)).thenReturn(valid(jwtAccessToken, randomValidDefaultClaims()));

        // Act
        this.componentUnderTest.verifyValidityOrThrow(jwtAccessToken);

        // Assert
        verify(this.securityJwtTokenUtility).validate(jwtAccessToken);
    }

    @Test
    public void verifyValidityAccessTokenThrowTest() {
        // Arrange
        var jwtAccessToken = entity(JwtAccessToken.class);
        when(this.securityJwtTokenUtility.validate(jwtAccessToken)).thenReturn(JwtTokenValidatedClaims.invalid(jwtAccessToken));

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyValidityOrThrow(jwtAccessToken));

        // Assert
        verify(this.securityJwtTokenUtility).validate(jwtAccessToken);
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(CookieAccessTokenInvalidException.class);
        assertThat(throwable).hasMessageContaining("JWT access token is invalid");
    }

    @Test
    public void verifyValidityRefreshTokenTest() throws CookieRefreshTokenInvalidException {
        // Arrange
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        when(this.securityJwtTokenUtility.validate(jwtRefreshToken)).thenReturn(valid(jwtRefreshToken, randomValidDefaultClaims()));

        // Act
        this.componentUnderTest.verifyValidityOrThrow(jwtRefreshToken);

        // Assert
        verify(this.securityJwtTokenUtility).validate(jwtRefreshToken);
    }

    @Test
    public void verifyValidityRefreshTokenThrowTest() {
        // Arrange
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        when(this.securityJwtTokenUtility.validate(jwtRefreshToken)).thenReturn(JwtTokenValidatedClaims.invalid(jwtRefreshToken));

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyValidityOrThrow(jwtRefreshToken));

        // Assert
        verify(this.securityJwtTokenUtility).validate(jwtRefreshToken);
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(CookieRefreshTokenInvalidException.class);
        assertThat(throwable).hasMessageContaining("JWT refresh token is invalid");
    }

    @ParameterizedTest
    @MethodSource("verifyAccessTokenExpirationOrThrow")
    public void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims, boolean expiredFlag, boolean throwableFlag) {
        // Arrange
        when(this.securityJwtTokenUtility.isExpired(eq(validatedClaims))).thenReturn(expiredFlag);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyAccessTokenExpirationOrThrow(validatedClaims));

        // Assert
        if (throwableFlag) {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(CookieAccessTokenExpiredException.class);
            assertThat(throwable).hasMessageContaining("JWT access token is expired. Username: " + validatedClaims.safeGetUsername());
        } else {
            verify(this.securityJwtTokenUtility).isExpired(eq(validatedClaims));
        }
        reset(
                this.securityJwtTokenUtility
        );
    }

    @ParameterizedTest
    @MethodSource("verifyRefreshTokenExpirationOrThrowTest")
    public void verifyRefreshTokenExpirationOrThrowTest(JwtTokenValidatedClaims validatedClaims, boolean expiredFlag, boolean throwableFlag) {
        // Arrange
        when(this.securityJwtTokenUtility.isExpired(eq(validatedClaims))).thenReturn(expiredFlag);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyRefreshTokenExpirationOrThrow(validatedClaims));

        // Assert
        if (throwableFlag) {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(CookieRefreshTokenExpiredException.class);
            assertThat(throwable).hasMessageContaining("JWT refresh token is expired. Username: " + validatedClaims.safeGetUsername());
        } else {
            verify(this.securityJwtTokenUtility).isExpired(eq(validatedClaims));
        }
        reset(
                this.securityJwtTokenUtility
        );
    }

    @Test
    public void verifyDbPresenceTest() throws CookieRefreshTokenDbNotFoundException {
        // Arrange
        var oldJwtRefreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = valid(oldJwtRefreshToken, randomValidDefaultClaims());
        var jwtUser = entity(JwtUser.class);
        when(this.jwtUserDetailsAssistant.loadUserByUsername(eq(validatedClaims.safeGetUsername().getIdentifier()))).thenReturn(jwtUser);
        when(this.userSessionRepository.isPresent(eq(oldJwtRefreshToken))).thenReturn(true);

        // Act
        var dbUser = this.componentUnderTest.verifyDbPresenceOrThrow(validatedClaims, oldJwtRefreshToken);

        // Assert
        verify(this.jwtUserDetailsAssistant).loadUserByUsername(eq(validatedClaims.safeGetUsername().getIdentifier()));
        verify(this.userSessionRepository).isPresent(eq(oldJwtRefreshToken));
        assertThat(dbUser).isEqualTo(jwtUser.getDbUser());
    }

    @Test
    public void verifyDbPresenceThrowTest() {
        // Arrange
        var oldJwtRefreshToken = entity(JwtRefreshToken.class);
        var validatedClaims = valid(oldJwtRefreshToken, randomValidDefaultClaims());
        var jwtUser = entity(JwtUser.class);
        when(this.jwtUserDetailsAssistant.loadUserByUsername(eq(validatedClaims.safeGetUsername().getIdentifier()))).thenReturn(jwtUser);
        when(this.userSessionRepository.isPresent(eq(oldJwtRefreshToken))).thenReturn(false);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.verifyDbPresenceOrThrow(validatedClaims, oldJwtRefreshToken));

        // Assert
        verify(this.jwtUserDetailsAssistant).loadUserByUsername(eq(validatedClaims.safeGetUsername().getIdentifier()));
        verify(this.userSessionRepository).isPresent(eq(oldJwtRefreshToken));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(CookieRefreshTokenDbNotFoundException.class);
        assertThat(throwable).hasMessageContaining("JWT refresh token is not present in database. Username: " + validatedClaims.safeGetUsername());
    }
}
