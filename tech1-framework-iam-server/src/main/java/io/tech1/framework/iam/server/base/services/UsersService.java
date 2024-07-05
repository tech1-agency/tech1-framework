package io.tech1.framework.iam.server.base.services;

import io.tech1.framework.iam.domain.jwt.JwtUser;

import java.util.List;

public interface UsersService {
    List<JwtUser> findAll();
}
