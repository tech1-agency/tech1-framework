package jbst.foundation.domain.base;

import jbst.foundation.utilities.strings.MaskUtility;

public record UsernamePasswordCredentials(
        Username username,
        Password password
) {
    public static UsernamePasswordCredentials mask5(Username username, Password password) {
        return new UsernamePasswordCredentials(username, Password.of(MaskUtility.mask5(password.value())));
    }

    public static UsernamePasswordCredentials hardcoded() {
        return new UsernamePasswordCredentials(Username.hardcoded(), Password.hardcoded());
    }

    public static UsernamePasswordCredentials random() {
        return new UsernamePasswordCredentials(Username.random(), Password.random());
    }
}
