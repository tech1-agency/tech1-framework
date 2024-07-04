package io.tech1.framework.b2b.mongodb.server.services.impl;

import io.tech1.framework.iam.domain.mongo.MongoDbUser;
import io.tech1.framework.iam.repositories.mongo.MongoUsersRepository;
import io.tech1.framework.b2b.mongodb.server.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersServiceImpl implements UsersService {

    // Repositories
    private final MongoUsersRepository mongoUsersRepository;

    @Override
    public List<MongoDbUser> findAll() {
        return this.mongoUsersRepository.findAll();
    }
}
