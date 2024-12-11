package jbst.iam.domain.functions;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import jbst.iam.domain.enums.AccountAccessMethod;

public record FunctionSessionRefreshedEmail(
        Username username,
        Email email,
        UserRequestMetadata requestMetadata
) {

    public static FunctionSessionRefreshedEmail hardcoded() {
        return new FunctionSessionRefreshedEmail(Username.hardcoded(), Email.hardcoded(), UserRequestMetadata.valid());
    }

    public FunctionAccountAccessed getFunctionAccountAccessed() {
        return new FunctionAccountAccessed(this.username, this.email, this.requestMetadata, AccountAccessMethod.SECURITY_TOKEN);
    }
}
