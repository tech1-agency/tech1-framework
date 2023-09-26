package io.tech1.framework.domain.http.requests;

import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.utilities.random.RandomUtility.localhost;
import static java.util.Objects.nonNull;

public record IPAddress(@NotNull String value) {

    public IPAddress(String value) {
        this.value = nonNull(value) ? value : localhost().value();
    }

}
