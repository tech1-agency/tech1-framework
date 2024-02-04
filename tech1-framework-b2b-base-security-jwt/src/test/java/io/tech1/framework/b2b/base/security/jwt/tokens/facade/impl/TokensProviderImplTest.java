package io.tech1.framework.b2b.base.security.jwt.tokens.facade.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.tokens.providers.TokensCookiesProvider;
import io.tech1.framework.b2b.base.security.jwt.tokens.providers.TokensHeadersProvider;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
class TokensProviderImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean("tokensCookiesProvider")
        TokensProvider tokensCookiesProvider() {
            return mock(TokensCookiesProvider.class);
        }

        @Bean("tokensHeadersProvider")
        TokensProvider tokensHeadersProvider() {
            return mock(TokensHeadersProvider.class);
        }

        @Bean("tokensProvider")
        TokensProvider tokensProvider() {
            return new TokensProviderImpl(
                    this.tokensCookiesProvider(),
                    this.tokensHeadersProvider()
            );
        }
    }

    private final TokensProvider tokensCookiesProvider;
    private final TokensProvider tokensHeadersProvider;

    private final TokensProvider componentUnderTest;

    @Autowired
    public TokensProviderImplTest(
            @Qualifier("tokensCookiesProvider") TokensProvider tokensCookiesProvider,
            @Qualifier("tokensHeadersProvider") TokensProvider tokensHeadersProvider,
            @Qualifier("tokensProvider") TokensProvider tokensProvider
    ) {
        this.tokensCookiesProvider = tokensCookiesProvider;
        this.tokensHeadersProvider = tokensHeadersProvider;
        this.componentUnderTest = tokensProvider;
    }

    @BeforeEach
    void beforeEach() {
        reset(
                this.tokensCookiesProvider,
                this.tokensHeadersProvider
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.tokensCookiesProvider,
                this.tokensHeadersProvider
        );
    }

    @Test
    void createResponseAccessToken() {
        // Arrange
        var jwtAccessToken = new JwtAccessToken(randomString());
        var httpServletResponse = mock(HttpServletResponse.class);

        // Act
        this.componentUnderTest.createResponseAccessToken(jwtAccessToken, httpServletResponse);

        // Assert
        verify(tokensCookiesProvider).createResponseAccessToken(jwtAccessToken, httpServletResponse);
    }

    @Test
    void createResponseRefreshToken() {
        // Arrange
        var refreshAccessToken = JwtRefreshToken.random();
        var httpServletResponse = mock(HttpServletResponse.class);

        // Act
        this.componentUnderTest.createResponseRefreshToken(refreshAccessToken, httpServletResponse);

        // Assert
        verify(tokensCookiesProvider).createResponseRefreshToken(refreshAccessToken, httpServletResponse);
    }

    @Test
    void readRequestAccessToken() throws AccessTokenNotFoundException {
        // Arrange
        var request = mock(HttpServletRequest.class);

        // Act
        this.componentUnderTest.readRequestAccessToken(request);

        // Assert
        verify(tokensCookiesProvider).readRequestAccessToken(request);
    }

    @Test
    void readRequestRefreshToken() throws RefreshTokenNotFoundException {
        // Arrange
        var request = mock(HttpServletRequest.class);

        // Act
        this.componentUnderTest.readRequestRefreshToken(request);

        // Assert
        verify(tokensCookiesProvider).readRequestRefreshToken(request);
    }

    @Test
    void clearTokens() {
        // Arrange
        var response = mock(HttpServletResponse.class);

        // Act
        this.componentUnderTest.clearTokens(response);

        // Assert
        verify(tokensCookiesProvider).clearTokens(response);
    }
}
