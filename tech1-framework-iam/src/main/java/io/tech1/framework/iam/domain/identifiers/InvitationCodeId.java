package io.tech1.framework.iam.domain.identifiers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static tech1.framework.foundation.domain.constants.StringConstants.UNKNOWN;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

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
