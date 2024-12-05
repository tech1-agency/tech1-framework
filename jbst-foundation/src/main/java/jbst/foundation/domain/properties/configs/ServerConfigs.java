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
    @NonMandatoryProperty
    private String webclientEmailConfirmRedirectPath;

    public static ServerConfigs hardcoded() {
        return new ServerConfigs(
                ServerName.hardcoded(),
                true,
                "http://127.0.0.1:3002/api",
                "http://127.0.0.1:3000",
                "/email-confirm"
        );
    }

    public static ServerConfigs random() {
        return new ServerConfigs(
                ServerName.random(),
                randomBoolean(),
                randomString(),
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

    public String getConfirmEmailURL(String token) {
        return "%s/jbst/security/tokens/email/confirm?token=%s".formatted(
                this.serverURL,
                token
        );
    }

    public String getEmailConfirmRedirectURL() {
        return this.webclientURL + this.webclientEmailConfirmRedirectPath;
    }
}
