package io.tech1.framework.b2b.mongodb.security.jwt.configurations;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface AbstractApplicationSecurityJwtConfigurer {
    void configure(WebSecurity web) throws Exception;
    void configure(HttpSecurity http) throws Exception;
}
