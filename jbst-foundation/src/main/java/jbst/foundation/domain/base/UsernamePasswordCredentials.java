package jbst.foundation.domain.base;

import jbst.foundation.utilities.strings.MaskUtility;

public record UsernamePasswordCredentials(
        Username username,
        Password password
) {
    public static UsernamePasswordCredentials mask5(Username username, Password password) {
        return new UsernamePasswordCredentials(username, Password.of(MaskUtility.mask5(password.value())));
    }

    public static UsernamePasswordCredentials random() {
        return new UsernamePasswordCredentials(Username.random(), Password.random());
    }

    public static UsernamePasswordCredentials testsHardcoded() {
        return new UsernamePasswordCredentials(Username.testsHardcoded(), Password.testsHardcoded());
    }
}
