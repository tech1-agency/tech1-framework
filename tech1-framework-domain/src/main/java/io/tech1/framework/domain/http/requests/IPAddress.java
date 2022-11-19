package io.tech1.framework.domain.http.requests;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.utilities.random.RandomUtility.localhost;
import static java.util.Objects.nonNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class IPAddress {
    private final String value;

    public IPAddress(
            String value
    ) {
        this.value = nonNull(value) ? value : localhost().getValue();
    }
}
