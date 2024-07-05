package io.tech1.framework.iam.services;

import io.tech1.framework.iam.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.iam.domain.functions.FunctionSessionRefreshedEmail;
import org.springframework.scheduling.annotation.Async;

public interface UsersEmailsService {
    @Async
    void executeAuthenticationLogin(FunctionAuthenticationLoginEmail function);
    @Async
    void executeSessionRefreshed(FunctionSessionRefreshedEmail function);
}
