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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.socket.WebSocketHandler;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CsrfInterceptorHandshakeTest {

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
    public void afterHandshakeTest() {
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
