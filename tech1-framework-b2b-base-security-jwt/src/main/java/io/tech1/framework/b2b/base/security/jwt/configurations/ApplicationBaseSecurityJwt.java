package io.tech1.framework.b2b.base.security.jwt.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.b2b.base.security.jwt.crons",
        "io.tech1.framework.b2b.base.security.jwt.utils",
        "io.tech1.framework.b2b.base.security.jwt.validators"
        // -------------------------------------------------------------------------------------------------------------
})
public class ApplicationBaseSecurityJwt {
}
