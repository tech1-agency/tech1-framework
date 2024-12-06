package jbst.iam.repositories;

import jbst.foundation.domain.base.Username;
import jbst.iam.domain.db.UserToken;
import jbst.iam.domain.dto.requests.RequestUserToken;
import jbst.iam.domain.enums.UserTokenType;
import jbst.iam.domain.identifiers.TokenId;

public interface UsersTokensRepository {
    UserToken findByValueAsAny(String value);
    UserToken findByUsernameValidOrNull(Username username, UserTokenType type);
    void cleanupExpired();
    void cleanupUsed();
    TokenId saveAs(UserToken token);
    UserToken saveAs(RequestUserToken request);
}
