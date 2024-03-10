package io.tech1.framework.domain.base;

public record UsernamePasswordCredentials(
        Username username,
        Password password
) {
    public static UsernamePasswordCredentials random() {
        return new UsernamePasswordCredentials(Username.random(), Password.random());
    }

    public static UsernamePasswordCredentials testsHardcoded() {
        return new UsernamePasswordCredentials(Username.testsHardcoded(), Password.testsHardcoded());
    }
}
