package jbst.iam.repositories;

import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.dto.requests.RequestUserEmailToken;
import jbst.iam.domain.identifiers.TokenId;

public interface UsersEmailsTokensRepository {
    UserEmailToken findByValueAsAny(String value);
    void cleanupExpired();
    TokenId saveAs(UserEmailToken token);
    UserEmailToken saveAs(RequestUserEmailToken request);
}
