package jbst.foundation.domain.properties.configs;

import jbst.foundation.domain.base.ServerName;
import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;
import static jbst.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final ServerName name;
    @MandatoryProperty
    private final Boolean springdocEnabled;
    @NonMandatoryProperty
    private String serverURL;
    @NonMandatoryProperty
    private String webclientURL;

    public static ServerConfigs hardcoded() {
        return new ServerConfigs(
                ServerName.hardcoded(),
                true,
                "http://127.0.0.1:3002",
                "http://127.0.0.1:3000"
        );
    }

    public static ServerConfigs random() {
        return new ServerConfigs(
                ServerName.random(),
                randomBoolean(),
                randomString(),
                randomString()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }

    public boolean isSpringdocEnabled() {
        return this.springdocEnabled;
    }

    public String getServerContextPathURL(String contextPath) {
        return this.serverURL + contextPath;
    }

}
