package jbst.iam.domain.functions;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;

public record FunctionSessionRefreshedEmail(
        Username username,
        Email email,
        UserRequestMetadata requestMetadata
) {

}
