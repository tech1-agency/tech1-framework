package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.domain.utilities.enums.EnumUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.EnumMap;
import java.util.Map;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;
import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.missingMappingsKeys;
import static io.tech1.framework.domain.utilities.random.RandomUtility.getEnumMapMappedRandomBoolean;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.collections4.SetUtils.disjunction;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentsConfigs extends AbstractPropertyConfigs {
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

    public static IncidentsConfigs random() {
        return new IncidentsConfigs(getEnumMapMappedRandomBoolean(SecurityJwtIncidentType.values()));
    }

    // TODO [YYL] double-check message
    @Override
    public void assertProperties(String propertyName) {
        super.assertProperties(propertyName);
        var disjunction = disjunction(this.typesConfigs.keySet(), EnumUtility.set(SecurityJwtIncidentType.class));
        assertTrueOrThrow(
                this.typesConfigs.size() == 9,
                missingMappingsKeys(
                        propertyName + ".typesConfigs",
                        baseJoining(SecurityJwtIncidentType.class),
                        baseJoining(disjunction)
                )
        );

        var loginFailureUsernamePassword = this.typesConfigs.get(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD);
        var loginFailureUsernameMaskedPassword = this.typesConfigs.get(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD);

        if (TRUE.equals(loginFailureUsernamePassword) && TRUE.equals(loginFailureUsernameMaskedPassword)) {
            throw new IllegalArgumentException("Please configure login failure incident feature. Only one feature type could be enabled");
        }
    }

    public boolean isEnabled(SecurityJwtIncidentType type) {
        return TRUE.equals(this.typesConfigs.get(type));
    }
}
