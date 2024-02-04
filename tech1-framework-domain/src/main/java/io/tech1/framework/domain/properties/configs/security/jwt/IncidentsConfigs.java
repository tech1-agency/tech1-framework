package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.DefaultUser;
import io.tech1.framework.domain.properties.base.DefaultUsers;
import io.tech1.framework.domain.properties.base.InvitationCodes;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.ZoneId;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;
import static java.lang.Boolean.TRUE;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final Map<SecurityJwtIncidentType, Boolean> typesConfigs;

    public static IncidentsConfigs testsHardcoded() {
        return new IncidentsConfigs(
                new EnumMap<>(
                        Map.of(
                                AUTHENTICATION_LOGIN, true,
                                AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD, false,
                                AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD, true,
                                AUTHENTICATION_LOGOUT, false,
                                AUTHENTICATION_LOGOUT_MIN, false,
                                SESSION_REFRESHED, true,
                                SESSION_EXPIRED, false,
                                REGISTER1, true,
                                REGISTER1_FAILURE, true
                        )
                )
        );
    }

    public boolean isEnabled(SecurityJwtIncidentType type) {
        return TRUE.equals(this.typesConfigs.get(type));
    }
}
