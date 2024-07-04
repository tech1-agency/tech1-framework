package io.tech1.framework.b2b.mongodb.server.services;

import io.tech1.framework.iam.domain.mongo.MongoDbUser;

import java.util.List;

public interface UsersService {
    List<MongoDbUser> findAll();
}
