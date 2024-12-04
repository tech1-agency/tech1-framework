package jbst.iam.domain.db;

import jbst.foundation.domain.base.Email;
import jbst.foundation.utilities.random.RandomUtility;
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

    public static UserEmailToken random() {
        return new UserEmailToken(
                TokenId.random(),
                Email.random(),
                RandomUtility.randomString(),
                RandomUtility.randomEnum(UserEmailTokenType.class),
                RandomUtility.randomLongGreaterThanZero()
        );
    }

    public boolean isExpired() {
        return isPast(this.expiryTimestamp);
    }

}
