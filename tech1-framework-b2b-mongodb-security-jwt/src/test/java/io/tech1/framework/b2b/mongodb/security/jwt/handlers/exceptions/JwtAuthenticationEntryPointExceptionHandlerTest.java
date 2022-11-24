package io.tech1.framework.b2b.mongodb.security.jwt.handlers.exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.HandlersContext;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.HttpRequestUtility;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.authetication.AuthenticationLoginFailureUsernameMaskedPasswordIncident;
import io.tech1.framework.incidents.domain.authetication.AuthenticationLoginFailureUsernamePasswordIncident;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
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
public class JwtAuthenticationEntryPointExceptionHandlerTest {

    @Configuration
    @Import({
            HandlersContext.class
    })
    static class ContextConfiguration {

    }

    private final IncidentPublisher incidentPublisher;
    private final HttpRequestUtility httpRequestUtility;
    private final ObjectMapper objectMapper;

    private final JwtAuthenticationEntryPointExceptionHandler componentUnderTest;

    @BeforeEach
    public void before() {
        reset(
                this.incidentPublisher,
                this.httpRequestUtility
        );
    }

    @AfterEach
    public void after() {
        verifyNoMoreInteractions(
                this.incidentPublisher,
                this.httpRequestUtility
        );
    }

    @Test
    public void commenceTest() throws IOException {
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
    public void commenceBadCredentialsExceptionNotCachedEndpointTest() throws IOException {
        // Arrange
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        var request = mock(HttpServletRequest.class);
        var badCredentialsException = mock(BadCredentialsException.class);
        when(badCredentialsException.getMessage()).thenReturn(randomString());
        when(this.httpRequestUtility.isCachedEndpoint(eq(request))).thenReturn(false);

        // Act
        this.componentUnderTest.commence(request, response, badCredentialsException);

        // Assert
        assertAndVerifyBasicCommence(
                request,
                response,
                printWriter,
                badCredentialsException
        );
        verify(this.httpRequestUtility).isCachedEndpoint(eq(request));
    }

    @Test
    public void commenceBadCredentialsExceptionCachedEndpointTest() throws IOException {
        // Arrange
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        var request = mock(HttpServletRequest.class);
        var badCredentialsException = mock(BadCredentialsException.class);
        when(badCredentialsException.getMessage()).thenReturn(randomString());
        when(this.httpRequestUtility.isCachedEndpoint(eq(request))).thenReturn(true);
        var usernameString = "admin11";
        var passwordString = "Admin11!";
        var payload = objectMapper.writeValueAsString(
                Map.of(
                        "username", usernameString,
                        "password", passwordString
                )
        );
        when(this.httpRequestUtility.getCachedPayload(eq(request))).thenReturn(payload);

        // Act
        this.componentUnderTest.commence(request, response, badCredentialsException);

        // Assert
        assertAndVerifyBasicCommence(
                request,
                response,
                printWriter,
                badCredentialsException
        );
        verify(this.httpRequestUtility).isCachedEndpoint(eq(request));
        verify(this.httpRequestUtility).getCachedPayload(eq(request));
        var username = Username.of(usernameString);
        var password = Password.of(passwordString);
        verify(this.incidentPublisher).publishAuthenticationLoginFailureUsernamePassword(eq(
                AuthenticationLoginFailureUsernamePasswordIncident.of(
                        username,
                        password
                )
        ));
        verify(this.incidentPublisher).publishAuthenticationLoginFailureUsernameMaskedPassword(eq(
                AuthenticationLoginFailureUsernameMaskedPasswordIncident.of(
                        username,
                        password
                )
        ));
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
        verify(response).setContentType(eq("application/json;charset=UTF-8"));
        verify(response).setStatus(eq(HttpServletResponse.SC_UNAUTHORIZED));
        verify(response).getWriter();
        verify(printWriter).write(jsonAC.capture());
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
        HashMap<String, Object> json = this.objectMapper.readValue(jsonAC.getValue(), typeRef);
        assertThat(json).hasSize(3);
        assertThat(json).containsKeys("exceptionEntityType", "attributes", "timestamp");
        assertThat(json.get("exceptionEntityType")).isEqualTo(ERROR.toString());
        var attributes = (Map<String, Object>) json.get("attributes");
        assertThat(attributes.get("shortMessage")).isEqualTo(exception.getMessage());
        assertThat(attributes.get("fullMessage")).isEqualTo(exception.getMessage());
        verify(exception, times(3)).getMessage();
        verifyNoMoreInteractions(
                request,
                response,
                exception
        );
    }
}
