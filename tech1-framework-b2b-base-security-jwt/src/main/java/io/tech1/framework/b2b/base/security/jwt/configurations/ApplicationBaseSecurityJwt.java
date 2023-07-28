package io.tech1.framework.b2b.base.security.jwt.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.b2b.base.security.jwt.cookies",
        "io.tech1.framework.b2b.base.security.jwt.crons",
        "io.tech1.framework.b2b.base.security.jwt.filters",
        "io.tech1.framework.b2b.base.security.jwt.handlers.exceptions",
        "io.tech1.framework.b2b.base.security.jwt.incidents.converters",
        "io.tech1.framework.b2b.base.security.jwt.utils",
        "io.tech1.framework.b2b.base.security.jwt.validators",
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.utilities.browsers",
        "io.tech1.framework.utilities.geo"
        // -------------------------------------------------------------------------------------------------------------
})
public class ApplicationBaseSecurityJwt {
}
