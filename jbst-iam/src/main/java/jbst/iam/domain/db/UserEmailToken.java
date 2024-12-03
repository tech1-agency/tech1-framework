package jbst.iam.domain.db;

import jbst.foundation.domain.base.Email;
import jbst.iam.domain.enums.UserEmailTokenType;
import jbst.iam.domain.identifiers.TokenId;

import static jbst.foundation.utilities.time.TimestampUtility.isPast;

public record UserEmailToken(
        TokenId id,
        Email email,
        String value,
        UserEmailTokenType type,
        long expiryTimestamp
) {

    public boolean isExpired() {
        return isPast(this.expiryTimestamp);
    }

}
