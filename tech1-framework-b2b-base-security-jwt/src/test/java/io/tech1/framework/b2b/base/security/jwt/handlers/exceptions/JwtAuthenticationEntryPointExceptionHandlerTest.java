package io.tech1.framework.b2b.base.security.jwt.handlers.exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLoginFailure;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationHandlersContext;
import io.tech1.framework.b2b.base.security.jwt.utils.HttpRequestUtils;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernameMaskedPassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernamePassword;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static io.tech1.framework.domain.exceptions.ExceptionEntityType.ERROR;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class JwtAuthenticationEntryPointExceptionHandlerTest {

    @Configuration
    @Import({
            TestsApplicationHandlersContext.class
    })
    static class ContextConfiguration {

    }

    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    private final HttpRequestUtils httpRequestUtils;
    private final ObjectMapper objectMapper;

    private final JwtAuthenticationEntryPointExceptionHandler componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtIncidentPublisher,
                this.securityJwtPublisher,
                this.httpRequestUtils
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtIncidentPublisher,
                this.securityJwtPublisher,
                this.httpRequestUtils
        );
    }

    @Test
    void commenceTest() throws IOException {
        // Arrange
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        var request = mock(HttpServletRequest.class);
        var authenticationException = mock(AuthenticationException.class);
        when(authenticationException.getMessage()).thenReturn(randomString());

        // Act
        this.componentUnderTest.commence(request, response, authenticationException);

        // Assert
        assertAndVerifyBasicCommence(
                request,
                response,
                printWriter,
                authenticationException
        );
    }

    @Test
    void commenceBadCredentialsExceptionNotCachedEndpointTest() throws IOException {
        // Arrange
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        var request = mock(HttpServletRequest.class);
        var badCredentialsException = mock(BadCredentialsException.class);
        when(badCredentialsException.getMessage()).thenReturn(randomString());
        when(this.httpRequestUtils.isCachedEndpoint(request)).thenReturn(false);

        // Act
        this.componentUnderTest.commence(request, response, badCredentialsException);

        // Assert
        assertAndVerifyBasicCommence(
                request,
                response,
                printWriter,
                badCredentialsException
        );
        verify(this.httpRequestUtils).isCachedEndpoint(request);
    }

    @Test
    void commenceBadCredentialsExceptionCachedEndpointTest() throws IOException {
        // Arrange
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        var request = mock(HttpServletRequest.class);
        var badCredentialsException = mock(BadCredentialsException.class);
        when(badCredentialsException.getMessage()).thenReturn(randomString());
        when(this.httpRequestUtils.isCachedEndpoint(request)).thenReturn(true);
        var usernameString = "admin11";
        var passwordString = "Admin11!";
        var payload = objectMapper.writeValueAsString(
                Map.of(
                        "username", usernameString,
                        "password", passwordString
                )
        );
        when(this.httpRequestUtils.getCachedPayload(request)).thenReturn(payload);

        // Act
        this.componentUnderTest.commence(request, response, badCredentialsException);

        // Assert
        assertAndVerifyBasicCommence(
                request,
                response,
                printWriter,
                badCredentialsException
        );
        verify(this.httpRequestUtils).isCachedEndpoint(request);
        verify(this.httpRequestUtils).getCachedPayload(request);
        var username = Username.of(usernameString);
        var password = Password.of(passwordString);
        verify(this.securityJwtPublisher).publishAuthenticationLoginFailure(
                new EventAuthenticationLoginFailure(
                        username
                )
        );
        verify(this.securityJwtIncidentPublisher).publishAuthenticationLoginFailureUsernamePassword(
                new IncidentAuthenticationLoginFailureUsernamePassword(
                        username,
                        password
                )
        );
        verify(this.securityJwtIncidentPublisher).publishAuthenticationLoginFailureUsernameMaskedPassword(
                new IncidentAuthenticationLoginFailureUsernameMaskedPassword(
                        username,
                        password
                )
        );
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    @SuppressWarnings("unchecked")
    private void assertAndVerifyBasicCommence(
            HttpServletRequest request,
            HttpServletResponse response,
            PrintWriter printWriter,
            Exception exception
    ) throws IOException {
        var jsonAC = ArgumentCaptor.forClass(String.class);
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).getWriter();
        verify(printWriter).write(jsonAC.capture());
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
        HashMap<String, Object> json = this.objectMapper.readValue(jsonAC.getValue(), typeRef);
        assertThat(json)
                .hasSize(3)
                .containsKeys("exceptionEntityType", "attributes", "timestamp")
                .containsEntry("exceptionEntityType", ERROR.toString());
        var attributes = (Map<String, Object>) json.get("attributes");
        assertThat(attributes)
                .containsEntry("shortMessage", exception.getMessage())
                .containsEntry("fullMessage", exception.getMessage());
        verify(exception, times(3)).getMessage();
        verifyNoMoreInteractions(
                request,
                response,
                exception
        );
    }
}
