
package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
public class RemoteServer {
    @MandatoryProperty
    private final String baseURL;
    @MandatoryProperty
    private final String username;
    @MandatoryProperty
    private final String password;

    public Username getUsername() {
        return new Username(this.username);
    }

    public Password getPassword() {
        return new Password(this.password);
    }
}
