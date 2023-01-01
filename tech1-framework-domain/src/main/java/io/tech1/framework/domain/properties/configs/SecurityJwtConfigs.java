package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Checkbox;
import io.tech1.framework.domain.properties.base.Mongodb;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.domain.properties.configs.security.jwt.*;
import io.tech1.framework.domain.utilities.enums.EnumUtility;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD;
import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD;
import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.missingMappingsKeys;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.collections4.SetUtils.disjunction;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityJwtConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private AuthoritiesConfigs authoritiesConfigs;
    @MandatoryProperty
    private CookiesConfigs cookiesConfigs;
    @MandatoryProperty
    private EssenceConfigs essenceConfigs;
    @MandatoryProperty
    private IncidentsConfigs incidentsConfigs;
    @MandatoryProperty
    private JwtTokensConfigs jwtTokensConfigs;
    @MandatoryProperty
    private LoggingConfigs loggingConfigs;
    @MandatoryProperty
    private Mongodb mongodb;
    @MandatoryProperty
    private SessionConfigs sessionConfigs;
    @MandatoryProperty
    private UsersEmailsConfigs usersEmailsConfigs;

    // NOTE: test-purposes
    public static SecurityJwtConfigs of(
            AuthoritiesConfigs authoritiesConfigs,
            CookiesConfigs cookiesConfigs,
            EssenceConfigs essenceConfigs,
            IncidentsConfigs incidentsConfigs,
            JwtTokensConfigs jwtTokensConfigs,
            LoggingConfigs loggingConfigs,
            Mongodb mongodb,
            SessionConfigs sessionConfigs,
            UsersEmailsConfigs usersEmailsConfigs
    ) {
        var instance = new SecurityJwtConfigs();
        instance.authoritiesConfigs = authoritiesConfigs;
        instance.cookiesConfigs = cookiesConfigs;
        instance.essenceConfigs = essenceConfigs;
        instance.incidentsConfigs = incidentsConfigs;
        instance.jwtTokensConfigs = jwtTokensConfigs;
        instance.loggingConfigs = loggingConfigs;
        instance.mongodb = mongodb;
        instance.sessionConfigs = sessionConfigs;
        instance.usersEmailsConfigs = usersEmailsConfigs;
        return instance;
    }

    // NOTE: test-purposes
    public static SecurityJwtConfigs disabledUsersEmailsConfigs() {
        var instance = new SecurityJwtConfigs();
        instance.usersEmailsConfigs = UsersEmailsConfigs.of(
                "[Tech1]",
                Checkbox.disabled(),
                Checkbox.disabled()
        );
        return instance;
    }

    @Override
    public void assertProperties() {
        super.assertProperties();

        var typesConfigs = this.incidentsConfigs.getTypesConfigs();
        var disjunction = disjunction(typesConfigs.keySet(), EnumUtility.set(SecurityJwtIncidentType.class));
        assertTrueOrThrow(
                typesConfigs.size() == 9,
                missingMappingsKeys(
                        "incidentsConfigs.typesConfigs",
                        baseJoining(SecurityJwtIncidentType.class),
                        baseJoining(disjunction)
                )
        );

        var loginFailureUsernamePassword = typesConfigs.get(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD);
        var loginFailureUsernameMaskedPassword = typesConfigs.get(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD);

        if (TRUE.equals(loginFailureUsernamePassword) && TRUE.equals(loginFailureUsernameMaskedPassword)) {
            throw new IllegalArgumentException("Please configure login failure incident feature. Only one feature type could be enabled");
        }
    }
}
