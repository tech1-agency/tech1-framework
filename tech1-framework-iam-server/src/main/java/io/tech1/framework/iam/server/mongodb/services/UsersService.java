package io.tech1.framework.iam.server.mongodb.services;

import io.tech1.framework.iam.domain.mongo.MongoDbUser;

import java.util.List;

public interface UsersService {
    List<MongoDbUser> findAll();
}
