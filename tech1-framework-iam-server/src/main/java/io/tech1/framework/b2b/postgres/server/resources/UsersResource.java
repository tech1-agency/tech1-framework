package io.tech1.framework.b2b.postgres.server.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tech1.framework.iam.domain.postgres.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.server.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Swagger
@Tag(name = "[tech1-postgres-server] Users API")
// Spring
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersResource {

    // Services
    private final UsersService usersService;

    @GetMapping("/server")
    public List<PostgresDbUser> findAll() {
        return this.usersService.findAll();
    }
}
