package tech1.framework.iam.services;

import tech1.framework.iam.domain.functions.FunctionAuthenticationLoginEmail;
import tech1.framework.iam.domain.functions.FunctionSessionRefreshedEmail;
import org.springframework.scheduling.annotation.Async;

public interface UsersEmailsService {
    @Async
    void executeAuthenticationLogin(FunctionAuthenticationLoginEmail function);
    @Async
    void executeSessionRefreshed(FunctionSessionRefreshedEmail function);
}
