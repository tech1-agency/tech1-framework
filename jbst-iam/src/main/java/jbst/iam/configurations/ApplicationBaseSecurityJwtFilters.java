package jbst.iam.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "tech1.framework.iam.filters.jwt",
        "tech1.framework.iam.filters.logging"
        // -------------------------------------------------------------------------------------------------------------
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationBaseSecurityJwtFilters {

}
