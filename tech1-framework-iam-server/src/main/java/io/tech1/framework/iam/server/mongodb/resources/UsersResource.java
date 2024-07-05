package io.tech1.framework.iam.server.mongodb.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tech1.framework.iam.domain.mongodb.MongoDbUser;
import io.tech1.framework.iam.server.mongodb.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Swagger
@Tag(name = "[tech1-mongodb-server] Users API")
// Spring
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersResource {

    // Services
    private final UsersService usersService;

    @GetMapping("/server")
    public List<MongoDbUser> findAll() {
        return this.usersService.findAll();
    }
}
