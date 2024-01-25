package io.tech1.framework.b2b.base.security.jwt.domain.identifiers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record InvitationCodeId(@NotNull String value) {

    @JsonCreator
    public static InvitationCodeId of(String value) {
        return new InvitationCodeId(value);
    }

    public static InvitationCodeId random() {
        return new InvitationCodeId(randomString());
    }

    public static InvitationCodeId unknown() {
        return of(UNKNOWN);
    }

    public static InvitationCodeId testsHardcoded() {
        return of("5EFCB2583361E1C7071E");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
