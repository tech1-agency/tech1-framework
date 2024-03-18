package io.tech1.framework.b2b.base.security.jwt.tests.runners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.b2b.base.security.jwt.handlers.exceptions.ResourceExceptionHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public abstract class AbstractResourcesRunner {

    protected final ObjectMapper objectMapper = new ObjectMapper();

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
