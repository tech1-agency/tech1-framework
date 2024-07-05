package io.tech1.framework.iam.server.mongodb.services.impl;

import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.mongodb.MongoDbUser;
import io.tech1.framework.iam.repositories.mongodb.MongoUsersRepository;
import io.tech1.framework.iam.server.base.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersServiceImpl implements UsersService {

    // Repositories
    private final MongoUsersRepository mongoUsersRepository;

    @Override
    public List<JwtUser> findAll() {
        return this.mongoUsersRepository.findAll().stream().map(MongoDbUser::asJwtUser).toList();
    }
}
