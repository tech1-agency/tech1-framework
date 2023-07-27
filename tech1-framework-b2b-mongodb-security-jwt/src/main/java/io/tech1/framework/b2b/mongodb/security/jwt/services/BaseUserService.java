package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate2;

public interface BaseUserService {
    void updateUser1(RequestUserUpdate1 requestUserUpdate1);
    void updateUser2(RequestUserUpdate2 requestUserUpdate2);
    void changePassword1(RequestUserChangePassword1 requestUserChangePassword1);
}
