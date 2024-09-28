package io.tech1.framework.iam.tests.runners;

import io.tech1.framework.iam.tests.contexts.TestsApplicationResourcesContext;
import io.tech1.framework.iam.configurations.ApplicationBaseSecurityJwtMvc;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ExtendWith({ SpringExtension.class })
@ContextConfiguration(classes = {
        ApplicationBaseSecurityJwtMvc.class,
        ApplicationFrameworkPropertiesTestsHardcodedContext.class,
        TestsApplicationResourcesContext.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractResourcesRunner1 extends AbstractResourcesRunner {

}
