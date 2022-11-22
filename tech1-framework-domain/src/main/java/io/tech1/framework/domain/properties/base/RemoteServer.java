
package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok (property-based)
@Data
public class RemoteServer {
    private String baseURL;
    private String username;
    private String password;

    // NOTE: test-purposes
    public static RemoteServer of(
            String baseURL,
            String username,
            String password
    ) {
        var instance = new RemoteServer();
        instance.baseURL = baseURL;
        instance.username = username;
        instance.password = password;
        return instance;
    }

    public Username getUsername() {
        return new Username(this.username);
    }

    public Password getPassword() {
        return new Password(this.password);
    }
}
