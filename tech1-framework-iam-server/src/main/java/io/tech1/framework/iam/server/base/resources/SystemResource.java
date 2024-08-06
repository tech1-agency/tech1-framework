package io.tech1.framework.iam.server.base.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Swagger
@Tag(name = "[tech1-server (?)] System API")
// Spring
@RestController
@RequestMapping("/system")
public class SystemResource {

    @GetMapping("/csrf")
    public String getCsrfToken(HttpServletRequest request) {
        var csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return csrf.getToken();
    }
}
