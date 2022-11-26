package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.Data;

import static java.util.Objects.nonNull;

// Lombok (property-based)
@Data
public class Mongodb {
    @MandatoryProperty
    private String host;
    @MandatoryProperty
    private int port;
    @MandatoryProperty
    private String database;
    @NonMandatoryProperty
    private String username;
    @NonMandatoryProperty
    private String password;

    // NOTE: test-purposes
    public static Mongodb of(
            String host,
            int port,
            String database
    ) {
        var instance = new Mongodb();
        instance.host = host;
        instance.port = port;
        instance.database = database;
        return instance;
    }

    public final String connectionString() {
        if (isAuthenticationRequired()) {
            return "mongodb://" + this.username + ":" + this.password + "@" + this.host + ":" + this.port + "/" + this.database;
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
