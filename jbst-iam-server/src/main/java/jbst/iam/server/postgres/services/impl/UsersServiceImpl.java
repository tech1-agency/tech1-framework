package jbst.iam.server.postgres.services.impl;

import tech1.framework.iam.domain.jwt.JwtUser;
import tech1.framework.iam.domain.postgres.db.PostgresDbUser;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import jbst.iam.server.base.services.UsersService;
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
