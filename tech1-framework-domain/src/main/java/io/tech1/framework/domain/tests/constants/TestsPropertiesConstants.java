package io.tech1.framework.domain.tests.constants;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.constants.DomainConstants;
import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.base.*;
import io.tech1.framework.domain.properties.configs.*;
import io.tech1.framework.domain.properties.configs.mvc.CorsConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.*;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.*;
import io.tech1.framework.domain.properties.configs.utilities.GeoLocationsConfigs;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

@UtilityClass
public class TestsPropertiesConstants {
    public static final ServerConfigs SERVER_CONFIGS = new ServerConfigs("tech1-spring-boot-server", "http://127.0.0.1:3000");
    public static final UtilitiesConfigs UTILITIES_CONFIGS = new UtilitiesConfigs(
            new GeoLocationsConfigs(
                    false
            )
    );
    public static final AsyncConfigs ASYNC_CONFIGS = new AsyncConfigs("tech1-async");
    public static final EventsConfigs EVENTS_CONFIGS = new EventsConfigs("tech1-events");
    public static final MvcConfigs MVC_CONFIGS = new MvcConfigs(
            true,
            "/framework/security",
            new CorsConfigs(
                    "/api/**",
                    new String[] { "http://localhost:8080", "http://localhost:8081" },
                    new String[] { "GET", "POST" },
                    new String[] { "Access-Control-Allow-Origin" },
                    true,
                    null
            )
    );
    public static final EmailConfigs EMAIL_CONFIGS = new EmailConfigs(
            false,
            "smtp.gmail.com",
            587,
            "Tech1",
            "tech1@gmail.com",
            "Password123!",
            new String[] { Email.random().value(), Email.random().value() }
    );
    public static final IncidentConfigs INCIDENT_CONFIGS = new IncidentConfigs(
            true,
            new RemoteServer(
                    "http://localhost:8973",
                    "incident-username",
                    "incident-password"
            )
    );
    public static final HardwareMonitoringConfigs HARDWARE_MONITORING_CONFIGS = new HardwareMonitoringConfigs(
            true,
            new EnumMap<>(
                    Map.of(
                            HardwareName.CPU, new BigDecimal("80"),
                            HardwareName.HEAP, new BigDecimal("85"),
                            HardwareName.SERVER, new BigDecimal("90"),
                            HardwareName.SWAP, new BigDecimal("95"),
                            HardwareName.VIRTUAL, new BigDecimal("98")
                    )
            )
    );
    public static final HardwareServerConfigs HARDWARE_SERVER_CONFIGS = new HardwareServerConfigs(
            "http://localhost:8484"
    );
    public static final SecurityJwtConfigs SECURITY_JWT_CONFIGS = new SecurityJwtConfigs(
            new AuthoritiesConfigs(
                    "io.tech1",
                    Set.of(
                            new Authority(SUPER_ADMIN),
                            new Authority("admin"),
                            new Authority("user"),
                            new Authority(INVITATION_CODE_READ),
                            new Authority(INVITATION_CODE_WRITE)
                    )
            ),
            new CookiesConfigs(DomainConstants.TECH1, new TimeAmount(5L, SECONDS)),
            new EssenceConfigs(
                    new DefaultUsers(
                            true,
                            List.of(
                                   new DefaultUser(
                                            "admin12",
                                            "password12",
                                            ZoneId.systemDefault(),
                                            null,
                                            Set.of("admin")
                                    )
                            )
                    ),
                    InvitationCodes.enabled()
            ),
            new IncidentsConfigs(
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
            ),
            new JwtTokensConfigs(
                    "TECH1",
                    JwtTokenStorageMethod.COOKIES,
                    new JwtToken(new TimeAmount(30L, SECONDS), "ajwt"),
                    new JwtToken(new TimeAmount(12L, HOURS), "rjwt")
            ),
            new LoggingConfigs(true),
            new SessionConfigs(
                    Cron.enabled("*/30 * * * * *", "Europe/Kiev"),
                    Cron.enabled("*/15 * * * * *", "Europe/Kiev")
            ),
            new UsersEmailsConfigs(
                    "[Tech1]",
                    Checkbox.enabled(),
                    Checkbox.enabled()
            )
    );

    public static final SecurityJwtWebsocketsConfigs SECURITY_JWT_WEBSOCKETS_CONFIGS = new SecurityJwtWebsocketsConfigs(
            new CsrfConfigs("csrf-cookie", "csrf-header", "csrf-parameter"),
            new StompEndpointRegistryConfigs("/endpoint"),
            new MessageBrokerRegistryConfigs("/app", "/queue", "/user"),
            new WebsocketsFeaturesConfigs(
                    new WebsocketsFeatureHardwareConfigs(true, "/account")
            )
    );

    public static final MongodbSecurityJwtConfigs MONGODB_SECURITY_JWT_CONFIGS = new MongodbSecurityJwtConfigs(
            Mongodb.noSecurity("127.0.0.1", 27017, "tech1_framework_server")
    );
}
