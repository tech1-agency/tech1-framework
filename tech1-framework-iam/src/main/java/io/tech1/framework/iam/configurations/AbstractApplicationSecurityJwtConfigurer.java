package io.tech1.framework.iam.configurations;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface AbstractApplicationSecurityJwtConfigurer {
    void configure(WebSecurity web);
    void configure(HttpSecurity http) throws Exception;
}
