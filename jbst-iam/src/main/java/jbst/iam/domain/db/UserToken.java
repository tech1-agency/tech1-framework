package jbst.iam.domain.db;

import jbst.foundation.domain.base.Email;
import jbst.foundation.utilities.random.RandomUtility;
import jbst.iam.domain.enums.UserTokenType;
import jbst.iam.domain.identifiers.TokenId;

import static jbst.foundation.utilities.time.TimestampUtility.isPast;

public record UserToken(
        TokenId id,
        Email email,
        String value,
        UserTokenType type,
        long expiryTimestamp
) {

    public static UserToken random() {
        return new UserToken(
                TokenId.random(),
                Email.random(),
                RandomUtility.randomString(),
                RandomUtility.randomEnum(UserTokenType.class),
                RandomUtility.randomLongGreaterThanZero()
        );
    }

    public boolean isExpired() {
        return isPast(this.expiryTimestamp);
    }

}
