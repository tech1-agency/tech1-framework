package jbst.iam.services;

import jbst.iam.domain.functions.FunctionAccountAccessed;
import jbst.iam.domain.functions.FunctionEmailConfirmation;
import jbst.iam.domain.functions.FunctionPasswordReset;
import org.springframework.scheduling.annotation.Async;

public interface UsersEmailsService {
    @Async
    void executeEmailConfirmation(FunctionEmailConfirmation function);
    @Async
    void executePasswordReset(FunctionPasswordReset function);
    @Async
    void executeAuthenticationLogin(FunctionAccountAccessed function);
    @Async
    void executeSessionRefreshed(FunctionAccountAccessed function);
}
