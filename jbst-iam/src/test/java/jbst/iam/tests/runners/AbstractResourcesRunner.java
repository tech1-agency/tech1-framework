package jbst.iam.tests.runners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jbst.iam.handlers.exceptions.ResourceExceptionHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public abstract class AbstractResourcesRunner {

    protected final ObjectMapper objectMapper = JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .build();

    protected MockMvc mvc;

    protected void standaloneSetupByResourceUnderTest(Object object) {
        this.mvc = MockMvcBuilders
                .standaloneSetup(object)
                .setControllerAdvice(new ResourceExceptionHandler())
                .build();
    }

    protected String getContent(Object value) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(value);
    }
}
