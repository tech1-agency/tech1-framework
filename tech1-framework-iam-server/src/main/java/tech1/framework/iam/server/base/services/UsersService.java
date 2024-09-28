package tech1.framework.iam.server.base.services;

import tech1.framework.iam.domain.jwt.JwtUser;

import java.util.List;

public interface UsersService {
    List<JwtUser> findAll();
}
