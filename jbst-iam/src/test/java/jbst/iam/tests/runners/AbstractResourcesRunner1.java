package jbst.iam.tests.runners;

import jbst.iam.configurations.ConfigurationBaseSecurityJwtMvc;
import jbst.iam.tests.contexts.TestsApplicationResourcesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;

@WebAppConfiguration
@ExtendWith({ SpringExtension.class })
@ContextConfiguration(classes = {
        ConfigurationBaseSecurityJwtMvc.class,
        ApplicationFrameworkPropertiesTestsHardcodedContext.class,
        TestsApplicationResourcesContext.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractResourcesRunner1 extends AbstractResourcesRunner {

}
