package io.tech1.framework.b2b.base.security.jwt.configurations;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface AbstractApplicationSecurityJwtConfigurer {
    void configure(WebSecurity web);
    void configure(HttpSecurity http) throws Exception;
}
