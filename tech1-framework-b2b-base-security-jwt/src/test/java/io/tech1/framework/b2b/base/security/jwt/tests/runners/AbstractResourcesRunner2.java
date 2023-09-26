package io.tech1.framework.b2b.base.security.jwt.tests.runners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.b2b.base.security.jwt.configurations.ApplicationBaseSecurityJwtMvc;
import io.tech1.framework.b2b.base.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.base.security.jwt.handlers.exceptions.ResourceExceptionHandler;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationPropertiesMocked;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationResourcesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;

@WebAppConfiguration
@ExtendWith({ SpringExtension.class })
@ContextConfiguration(classes = {
        ApplicationBaseSecurityJwtMvc.class,
        TestsApplicationPropertiesMocked.class,
        TestsApplicationResourcesContext.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractResourcesRunner2 {

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

    protected static CurrentClientUser randomCurrentClientUser() {
        return new CurrentClientUser(
                randomUsername(),
                randomEmail(),
                randomString(),
                randomZoneId(),
                new ArrayList<>(),
                new HashMap<>()
        );
    }
}
