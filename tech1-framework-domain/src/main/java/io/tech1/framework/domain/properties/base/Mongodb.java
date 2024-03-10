package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static java.util.Objects.nonNull;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class Mongodb extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final String host;
    @MandatoryProperty
    private final Integer port;
    @MandatoryProperty
    private final String database;
    @NonMandatoryProperty
    private Username username;
    @NonMandatoryProperty
    private Password password;

    public static Mongodb testsHardcoded() {
        return Mongodb.noSecurity("127.0.0.1", 27017, "tech1_framework_server");
    }

    public static Mongodb random() {
        return Mongodb.noSecurity(randomIPv4(), randomIntegerGreaterThanZeroByBounds(26000, 30000), randomString());
    }

    public static Mongodb noSecurity(String host, int port, String database) {
        return new Mongodb(host, port, database, null, null);
    }

    public final String connectionString() {
        if (isAuthenticationRequired()) {
            return "mongodb://" + this.username.value() + ":" + this.password.value() + "@" + this.host + ":" + this.port + "/" + this.database;
        } else {
            return "mongodb://" + this.host + ":" + this.port + "/" + this.database;
        }
    }

    // ================================================================================================================
    // PRIVATE METHODS
    // ================================================================================================================
    private boolean isAuthenticationRequired() {
        return nonNull(this.username) && nonNull(this.password);
    }
}
