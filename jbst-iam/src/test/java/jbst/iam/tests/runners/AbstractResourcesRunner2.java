package jbst.iam.tests.runners;

import jbst.iam.configurations.ApplicationBaseSecurityJwtMvc;
import jbst.iam.tests.contexts.TestsApplicationPropertiesMocked;
import jbst.iam.tests.contexts.TestsApplicationResourcesContext;
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
        TestsApplicationPropertiesMocked.class,
        TestsApplicationResourcesContext.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractResourcesRunner2 extends AbstractResourcesRunner {

}
