package io.tech1.framework.b2b.postgres.server.resources;

import io.tech1.framework.b2b.base.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersResource {

    private final PostgresUsersRepository postgresUsersRepository;

    @GetMapping
    public List<CurrentClientUser> findAll() {
        return this.postgresUsersRepository.findAll().stream()
                .map(user -> new CurrentClientUser(user.getUsername(), user.getEmail(), user.getName(), user.getZoneId(), user.getAuthorities(), user.getAttributes()))
                .toList();
    }
}
