package io.tech1.framework.domain.tests.constants;

import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.base.*;
import io.tech1.framework.domain.properties.configs.*;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeaturesConfigs;
import io.tech1.framework.domain.properties.configs.mvc.CorsConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.*;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.*;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEmail;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

@UtilityClass
public class TestsPropertiesConstants {
    public static final AsyncConfigs ASYNC_CONFIGS = AsyncConfigs.of("tech1-async");
    public static final EventsConfigs EVENTS_CONFIGS = EventsConfigs.of("tech1-events");
    public static final MvcConfigs MVC_CONFIGS = MvcConfigs.of(
            true,
            "/platform/security",
            CorsConfigs.of(
                    true,
                    "/api/**",
                    new String[] { "http://localhost:8080", "http://localhost:8081" },
                    new String[] { "GET", "POST" },
                    new String[] { "Access-Control-Allow-Origin" },
                    true,
                    null
            )
    );
    public static final EmailConfigs EMAIL_CONFIGS = EmailConfigs.of(
            false,
            "smtp.gmail.com",
            587,
            "tech1@gmail.com",
            "Tech1",
            "Password123!",
            new String[] { randomEmail(), randomEmail() }
    );
    public static final IncidentConfigs INCIDENT_CONFIGS = IncidentConfigs.of(
            true,
            RemoteServer.of(
                    "http://localhost:8973",
                    "incident-user",
                    "incident-password"
            ),
            IncidentFeaturesConfigs.of(
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs()
            )
    );
    public static final HardwareMonitoringConfigs HARDWARE_MONITORING_CONFIGS = HardwareMonitoringConfigs.of(
            Map.of(
                    HardwareName.CPU, new BigDecimal("80"),
                    HardwareName.HEAP, new BigDecimal("85"),
                    HardwareName.SERVER, new BigDecimal("90"),
                    HardwareName.SWAP, new BigDecimal("95"),
                    HardwareName.VIRTUAL, new BigDecimal("98")
            )
    );
    public static final HardwareServerConfigs HARDWARE_SERVER_CONFIGS = HardwareServerConfigs.of(
            "http://localhost:8484"
    );
    public static final SecurityJwtConfigs SECURITY_JWT_CONFIGS = SecurityJwtConfigs.of(
            AuthoritiesConfigs.of(
                    "io.tech1",
                    Set.of(
                            Authority.of(SUPER_ADMIN),
                            Authority.of("admin"),
                            Authority.of("user"),
                            Authority.of(INVITATION_CODE_READ),
                            Authority.of(INVITATION_CODE_WRITE)
                    )
            ),
            CookiesConfigs.of("tech1.io", TimeAmount.of(5L, SECONDS)),
            EssenceConfigs.of(
                    DefaultUsers.of(
                            true,
                            List.of(
                                    DefaultUser.of(
                                            "admin12",
                                            "password12",
                                            ZoneId.systemDefault(),
                                            List.of("admin")
                                    )
                            )
                    ),
                    InvitationCodes.of(true)
            ),
            JwtTokensConfigs.of(
                    "TECH1",
                    JwtToken.of("ajwt", TimeAmount.of(30L, SECONDS)),
                    JwtToken.of("rjwt", TimeAmount.of(12L, HOURS))
            ),
            LoggingConfigs.of(true),
            Mongodb.of("127.0.0.1", 27017, "tech1_platform_server"),
            SessionConfigs.of(
                    Cron.of(false, "*/30 * * * * *", "Europe/Kiev")
            )
    );
    public static final SecurityJwtWebsocketsConfigs SECURITY_JWT_WEBSOCKETS_CONFIGS = SecurityJwtWebsocketsConfigs.of(
            CsrfConfigs.of("cookieName", "headerName", "parameterName"),
            StompEndpointRegistryConfigs.of("/endpoint"),
            MessageBrokerRegistryConfigs.of("/app", "/queue", "/user"),
            WebsocketsFeaturesConfigs.of(
                    WebsocketsFeatureHardwareConfigs.of(true, "/platform")
            )
    );
}
