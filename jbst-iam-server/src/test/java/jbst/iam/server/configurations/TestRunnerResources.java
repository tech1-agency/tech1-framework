package jbst.iam.server.configurations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jbst.iam.handlers.exceptions.ResourceExceptionHandler;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebAppConfiguration
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(classes = {
        TestConfigurationResources.class
})
public abstract class TestRunnerResources {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected MockMvc mvc;

    public void beforeByResource(Object object) {
        this.mvc = MockMvcBuilders
                .standaloneSetup(object)
                .setControllerAdvice(new ResourceExceptionHandler())
                .build();
    }

    @SuppressWarnings("unused")
    protected String getContent(Object value) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(value);
    }
}
