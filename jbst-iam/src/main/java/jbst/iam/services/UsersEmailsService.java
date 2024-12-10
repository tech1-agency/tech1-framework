package jbst.iam.services;

import jbst.iam.domain.functions.FunctionAuthenticationLoginEmail;
import jbst.iam.domain.functions.FunctionEmailConfirmation;
import jbst.iam.domain.functions.FunctionPasswordReset;
import jbst.iam.domain.functions.FunctionSessionRefreshedEmail;
import org.springframework.scheduling.annotation.Async;

public interface UsersEmailsService {
    @Async
    void executeEmailConfirmation(FunctionEmailConfirmation function);
    @Async
    void executePasswordReset(FunctionPasswordReset function);
    @Async
    void executeAuthenticationLogin(FunctionAuthenticationLoginEmail function);
    @Async
    void executeSessionRefreshed(FunctionSessionRefreshedEmail function);
}
