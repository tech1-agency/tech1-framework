package io.tech1.framework.b2b.mongodb.security.jwt.handlers.exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.TestsApplicationHandlersContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
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
public class JwtAccessDeniedExceptionHandlerTest {

    @Configuration
    @Import({
            TestsApplicationHandlersContext.class
    })
    static class ContextConfiguration {

    }

    private final ObjectMapper objectMapper;
    private final JwtAccessDeniedExceptionHandler componentUnderTest;

    @SuppressWarnings("unchecked")
    @Test
    public void handleTest() throws IOException {
        // Arrange
        var httpServletResponse = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        var httpServletRequest = mock(HttpServletRequest.class);
        var exception = mock(AccessDeniedException.class);
        var exceptionMessage = randomString();
        when(exception.getMessage()).thenReturn(exceptionMessage);
        var jsonAC = ArgumentCaptor.forClass(String.class);

        // Act
        this.componentUnderTest.handle(httpServletRequest, httpServletResponse, exception);

        // Assert
        verify(httpServletResponse).setContentType(eq("application/json;charset=UTF-8"));
        verify(httpServletResponse).setStatus(eq(HttpServletResponse.SC_FORBIDDEN));
        verify(httpServletResponse).getWriter();
        verify(exception).getMessage();
        verify(printWriter).write(jsonAC.capture());
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
        HashMap<String, Object> json = objectMapper.readValue(jsonAC.getValue(), typeRef);
        assertThat(json).hasSize(3);
        assertThat(json).containsKeys("exceptionEntityType", "attributes", "timestamp");
        assertThat(json.get("exceptionEntityType")).isEqualTo(ERROR.toString());
        var attributes = (Map<String, Object>) json.get("attributes");
        assertThat(attributes.get("shortMessage")).isEqualTo(exceptionMessage);
        assertThat(attributes.get("fullMessage")).isEqualTo(exceptionMessage);
        verifyNoMoreInteractions(
                httpServletRequest,
                httpServletResponse,
                exception
        );
    }
}
