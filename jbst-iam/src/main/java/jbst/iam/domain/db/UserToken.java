package jbst.iam.domain.db;

import jbst.foundation.domain.base.Username;
import jbst.foundation.utilities.random.RandomUtility;
import jbst.iam.domain.enums.UserTokenType;
import jbst.iam.domain.identifiers.TokenId;

import static jbst.foundation.utilities.time.TimestampUtility.isPast;

public record UserToken(
        TokenId id,
        Username username,
        String value,
        UserTokenType type,
        long expiryTimestamp,
        boolean used
) {

    public static UserToken random() {
        return new UserToken(
                TokenId.random(),
                Username.random(),
                RandomUtility.randomString(),
                RandomUtility.randomEnum(UserTokenType.class),
                RandomUtility.randomLongGreaterThanZero(),
                RandomUtility.randomBoolean()
        );
    }

    public UserToken withUsed(boolean used) {
        return new UserToken(
                this.id,
                this.username,
                this.value,
                this.type,
                this.expiryTimestamp,
                used
        );
    }

    public boolean isExpired() {
        return isPast(this.expiryTimestamp);
    }

}
