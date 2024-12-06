package jbst.foundation.domain.properties.configs.security.jwt;

import jbst.foundation.domain.properties.annotations.NonMandatoryProperty;
import jbst.foundation.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class UsersTokensConfigs extends AbstractPropertiesConfigs {
    @NonMandatoryProperty
    private String webclientEmailConfirmRedirectPath;

    public static UsersTokensConfigs hardcoded() {
        return new UsersTokensConfigs(
                "/email-confirm"
        );
    }

    public static UsersTokensConfigs random() {
        return new UsersTokensConfigs(
                randomString()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return false;
    }

    public String getConfirmEmailURL(
            String serverURL,
            String basePathPrefix,
            String token
    ) {
        return "%s%s/tokens/email/confirm?token=%s".formatted(
                serverURL,
                basePathPrefix,
                token
        );
    }

    public String getEmailConfirmRedirectURL(String webclientURL) {
        return webclientURL + this.webclientEmailConfirmRedirectPath;
    }
}
