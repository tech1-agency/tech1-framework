package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;

public interface BaseUsersService {
    void updateUser1(JwtUser user, RequestUserUpdate1 request);
    void updateUser2(JwtUser user, RequestUserUpdate2 request);
    void changePasswordRequired(JwtUser user, RequestUserChangePasswordBasic request);
    void changePassword1(JwtUser user, RequestUserChangePasswordBasic request);
}
