package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;

public interface BaseUsersService {
    void updateUser1(JwtUser user, RequestUserUpdate1 requestUserUpdate1);
    void updateUser2(JwtUser user, RequestUserUpdate2 requestUserUpdate2);
    void changePassword1(JwtUser user, RequestUserChangePassword1 requestUserChangePassword1);
}
