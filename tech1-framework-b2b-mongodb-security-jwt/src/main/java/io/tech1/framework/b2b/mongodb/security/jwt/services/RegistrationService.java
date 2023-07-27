package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;

public interface RegistrationService {
    DbUser register1(RequestUserRegistration1 requestUserRegistration1);
}
