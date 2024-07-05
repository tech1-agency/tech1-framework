package io.tech1.framework.iam.server.postgres.services.impl;

import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.postgres.db.PostgresDbUser;
import io.tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import io.tech1.framework.iam.server.base.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersServiceImpl implements UsersService {

    // Repositories
    private final PostgresUsersRepository postgresUsersRepository;

    @Override
    public List<JwtUser> findAll() {
        return this.postgresUsersRepository.findAll().stream().map(PostgresDbUser::asJwtUser).toList();
    }
}
