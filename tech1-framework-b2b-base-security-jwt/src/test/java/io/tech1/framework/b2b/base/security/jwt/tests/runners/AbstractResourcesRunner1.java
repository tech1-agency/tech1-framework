package io.tech1.framework.b2b.base.security.jwt.tests.runners;

import io.tech1.framework.b2b.base.security.jwt.configurations.ApplicationBaseSecurityJwtMvc;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationResourcesContext;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
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
