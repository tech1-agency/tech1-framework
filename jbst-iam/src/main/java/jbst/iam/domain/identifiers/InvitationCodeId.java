package jbst.iam.domain.identifiers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jbst.foundation.domain.constants.JbstConstants;
import org.jetbrains.annotations.NotNull;

import static jbst.foundation.utilities.random.RandomUtility.randomString;

public record InvitationCodeId(@NotNull String value) {

    @JsonCreator
    public static InvitationCodeId of(String value) {
        return new InvitationCodeId(value);
    }

    public static InvitationCodeId hardcoded() {
        return of("5EFCB2583361E1C7071E");
    }

    public static InvitationCodeId random() {
        return new InvitationCodeId(randomString());
    }

    @SuppressWarnings("unused")
    public static InvitationCodeId unknown() {
        return of(JbstConstants.Strings.UNKNOWN);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
