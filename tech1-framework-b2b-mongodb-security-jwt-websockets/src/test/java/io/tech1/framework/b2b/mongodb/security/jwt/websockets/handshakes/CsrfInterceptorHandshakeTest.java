package io.tech1.framework.b2b.mongodb.security.jwt.websockets.handshakes;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.socket.WebSocketHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static io.tech1.framework.domain.utilities.http.HttpCookieUtility.createCookie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CsrfInterceptorHandshakeTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        CsrfInterceptorHandshake csrfInterceptorHandshake() {
            return new CsrfInterceptorHandshake(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final CsrfInterceptorHandshake componentUnderTest;

    @Test
    void beforeHandshakeRuntimeExceptionTest() {
        // Arrange
        var request = Mockito.mock(ServletServerHttpRequest.class);
        var response = Mockito.mock(ServerHttpResponse.class);
        var wsHandler = Mockito.mock(WebSocketHandler.class);
        Map<String, Object> attributes = new HashMap<>();

        // Act
        var actual = this.componentUnderTest.beforeHandshake(request, response, wsHandler, attributes);

        // Assert
        assertThat(actual).isFalse();
        assertThat(attributes).isEmpty();
        verify(request).getServletRequest();
        verifyNoMoreInteractions(
                request,
                request,
                wsHandler
        );
    }

    @Test
    void beforeHandshakeNoCookieExceptionTest() {
        // Arrange
        var request = Mockito.mock(ServletServerHttpRequest.class);
        var httpRequest = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(ServerHttpResponse.class);
        var wsHandler = Mockito.mock(WebSocketHandler.class);
        Map<String, Object> attributes = new HashMap<>();
        when(request.getServletRequest()).thenReturn(httpRequest);

        // Act
        var actual = this.componentUnderTest.beforeHandshake(request, response, wsHandler, attributes);

        // Assert
        assertThat(actual).isFalse();
        assertThat(attributes).isEmpty();
        verify(request).getServletRequest();
        verifyNoMoreInteractions(
                request,
                request,
                wsHandler
        );
    }

    @Test
    void beforeHandshakeTest() {
        // Arrange
        var request = Mockito.mock(ServletServerHttpRequest.class);
        var httpRequest = Mockito.mock(HttpServletRequest.class);
        var cookie = createCookie("csrf-cookie", "value123", "tech1.io", false, 120);
        var response = Mockito.mock(ServerHttpResponse.class);
        var wsHandler = Mockito.mock(WebSocketHandler.class);
        Map<String, Object> attributes = new HashMap<>();
        when(httpRequest.getCookies()).thenReturn(new Cookie[] { cookie });
        when(request.getServletRequest()).thenReturn(httpRequest);

        // Act
        var actual = this.componentUnderTest.beforeHandshake(request, response, wsHandler, attributes);

        // Assert
        assertThat(actual).isTrue();
        assertThat(attributes).hasSize(1);
        var csrfToken = (DefaultCsrfToken) attributes.get(CsrfToken.class.getName());
        assertThat(csrfToken.getToken()).isEqualTo("value123");
        assertThat(csrfToken.getHeaderName()).isEqualTo("csrf-header");
        assertThat(csrfToken.getParameterName()).isEqualTo("csrf-parameter");
        verify(request).getServletRequest();
        verifyNoMoreInteractions(
                request,
                request,
                wsHandler
        );
    }


    @Test
    void afterHandshakeTest() {
        // Arrange
        var request = Mockito.mock(ServerHttpRequest.class);
        var response = Mockito.mock(ServerHttpResponse.class);
        var wsHandler = Mockito.mock(WebSocketHandler.class);
        var exception = Mockito.mock(Exception.class);

        // Act
        this.componentUnderTest.afterHandshake(request, response, wsHandler, exception);

        // Assert
        verifyNoMoreInteractions(
                request,
                request,
                wsHandler,
                exception
        );
    }
}
