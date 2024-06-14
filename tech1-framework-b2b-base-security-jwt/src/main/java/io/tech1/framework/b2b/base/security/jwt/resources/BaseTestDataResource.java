package io.tech1.framework.b2b.base.security.jwt.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Swagger
@Tag(name = "[tech1-framework] Test Data API")
// Spring
@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/test-data")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseTestDataResource {

    @GetMapping("/sessions")
    public ResponseUserSessionsTable getSessions() {
        return ResponseUserSessionsTable.random();
    }
}

