package io.tech1.framework.domain.properties.utilties;

import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.base.ScheduledJob;
import io.tech1.framework.domain.properties.base.SchedulerConfiguration;
import io.tech1.framework.domain.properties.base.SpringLogging;
import io.tech1.framework.domain.properties.base.SpringServer;
import io.tech1.framework.domain.properties.configs.*;
import io.tech1.framework.domain.properties.configs.security.jwt.IncidentsConfigs;
import io.tech1.framework.domain.tests.classes.NotUsedPropertiesConfigs;
import io.tech1.framework.domain.utilities.collections.CollectorUtility;
import io.tech1.framework.domain.utilities.enums.EnumUtility;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class PropertiesAsserterAndPrinterTest {

    @Test
    void notUsedPropertiesConfigsTest() {
        // Arrange
        var notUsedPropertiesConfigs = new NotUsedPropertiesConfigs(
                new ScheduledJob(true, SchedulerConfiguration.testsHardcoded()),
                new SpringServer(8080),
                new SpringLogging("logback-test.xml")
        );

        // Act
        notUsedPropertiesConfigs.assertProperties("notUsedPropertiesConfigs");
        notUsedPropertiesConfigs.printProperties("notUsedPropertiesConfigs");

        // Assert
        // no asserts
    }

    @Test
    void serverConfigsTest() {
        // Act
        ServerConfigs.testsHardcoded().assertProperties("serverConfigs");
        ServerConfigs.testsHardcoded().printProperties("serverConfigs");

        // Assert
        // no asserts
    }

    @Test
    void utilitiesConfigsTest() {
        // Act
        UtilitiesConfigs.testsHardcoded().assertProperties("utilitiesConfigs");
        UtilitiesConfigs.testsHardcoded().printProperties("utilitiesConfigs");

        // Assert
        // no asserts
    }

    @Test
    void asyncConfigsTest() {
        // Act
        AsyncConfigs.testsHardcoded().assertProperties("asyncConfigs");
        AsyncConfigs.testsHardcoded().printProperties("asyncConfigs");

        // Assert
        // no asserts
    }

    @Test
    void eventsConfigsTest() {
        // Act
        EventsConfigs.testsHardcoded().assertProperties("eventsConfigs");
        EventsConfigs.testsHardcoded().printProperties("eventsConfigs");

        // Assert
        // no asserts
    }

    @Test
    void mvcConfigsDisabledTest() {
        // Arrange
        var mvcConfigs = new MvcConfigs(false, null, null);

        // Act
        mvcConfigs.assertProperties("mvcConfigs");
        mvcConfigs.printProperties("mvcConfigs");

        // Assert
        // no asserts
    }

    @Test
    void mvcConfigsTest() {
        // Act
//        MvcConfigs.testsHardcoded().assertProperties("mvcConfigs");
        MvcConfigs.testsHardcoded().printProperties("mvcConfigs");

        // Assert
        // no asserts
    }

    @Test
    void emailConfigsDisabledTest() {
        // Act
        EmailConfigs.disabled().assertProperties("emailConfigs");
        EmailConfigs.disabled().printProperties("emailConfigs");

        // Assert
        // no asserts
    }

    @Test
    void emailConfigsTest() {
        // Act
        EmailConfigs.testsHardcoded().assertProperties("emailConfigs");
        EmailConfigs.testsHardcoded().printProperties("emailConfigs");

        // Assert
        // no asserts
    }

    @Test
    void incidentConfigsTest() {
        // Act
        IncidentConfigs.testsHardcoded().assertProperties("incidentConfigs");
        IncidentConfigs.testsHardcoded().printProperties("incidentConfigs");

        // Assert
        // no asserts
    }

    @Test
    void hardwareMonitoringConfigsDisabledTest() {
        // Arrange
        var hardwareMonitoringConfigs = HardwareMonitoringConfigs.disabled();

        // Act
        HardwareMonitoringConfigs.disabled().assertProperties("hardwareMonitoringConfigs");

        // Assert
        var thresholdsConfigs = hardwareMonitoringConfigs.getThresholdsConfigs();
        assertThat(thresholdsConfigs).hasSize(5);
        assertThat(thresholdsConfigs.keySet()).isEqualTo(EnumUtility.set(HardwareName.class));
        assertThat(thresholdsConfigs.values().stream().distinct().collect(CollectorUtility.toSingleton())).isEqualTo(ZERO);
    }

    @Test
    void hardwareMonitoringConfigsExceptionTest() {
        // Arrange
        var hardwareMonitoringConfigs = new HardwareMonitoringConfigs(
                true,
                new HashMap<>() {{
                    put(HardwareName.CPU, new BigDecimal("80"));
                    put(HardwareName.HEAP, new BigDecimal("85"));
                }}
        );

        // Act
        var throwable = catchThrowable(() -> hardwareMonitoringConfigs.assertProperties("hardwareMonitoringConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Attribute `hardwareMonitoringConfigs.thresholdsConfigs` requirements: `[CPU, HEAP, SERVER, SWAP, VIRTUAL]`, disjunction: `[SERVER, SWAP, VIRTUAL]`");
    }

    @Test
    void hardwareMonitoringConfigsTest() {
        // Act
        HardwareMonitoringConfigs.testsHardcoded().assertProperties("hardwareMonitoringConfigs");
        HardwareMonitoringConfigs.testsHardcoded().printProperties("hardwareMonitoringConfigs");

        // Assert
        var thresholdsConfigs = HardwareMonitoringConfigs.testsHardcoded().getThresholdsConfigs();
        assertThat(thresholdsConfigs).hasSize(5);
        assertThat(thresholdsConfigs.keySet()).isEqualTo(EnumUtility.set(HardwareName.class));
        assertThat(new HashSet<>(thresholdsConfigs.values())).hasSize(5);
    }

    @Test
    void hardwareServerConfigsTest() {
        // Act
        HardwareServerConfigs.testsHardcoded().assertProperties("hardwareServerConfigs");
        HardwareServerConfigs.testsHardcoded().printProperties("hardwareServerConfigs");

        // Assert
        // no asserts
    }

    @RepeatedTest(5)
    void securityJwtConfigsDisabledUsersEmailsConfigsTest() {
        // Act
        var securityJwtConfigs = SecurityJwtConfigs.disabledUsersEmailsConfigs();

        // Act
        var throwable = catchThrowable(() -> securityJwtConfigs.assertProperties("securityJwtConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).startsWith("Attribute `securityJwtConfigs.");
        assertThat(throwable.getMessage()).endsWith("` is invalid");
    }

    @Test
    void securityJwtConfigsTest() {
        // Act
        SecurityJwtConfigs.testsHardcoded().assertProperties("securityJwtConfigs");
        SecurityJwtConfigs.testsHardcoded().printProperties("securityJwtConfigs");

        // Assert
        // no asserts
    }

    @Test
    void securityJwtConfigsIncidentsCorrectTest() {
        var loginFailureUsernamePassword = randomBoolean();
        var loginFailureUsernameMaskedPassword = !loginFailureUsernamePassword;
        var incidentConfigs = new IncidentsConfigs(
                new HashMap<>() {{
                    put(AUTHENTICATION_LOGIN, randomBoolean());
                    put(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD, loginFailureUsernamePassword);
                    put(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD, loginFailureUsernameMaskedPassword);
                    put(AUTHENTICATION_LOGOUT, randomBoolean());
                    put(AUTHENTICATION_LOGOUT_MIN, randomBoolean());
                    put(SESSION_REFRESHED, randomBoolean());
                    put(SESSION_EXPIRED, randomBoolean());
                    put(REGISTER1, randomBoolean());
                    put(REGISTER1_FAILURE, randomBoolean());
                }}
        );
        var securityJwtConfigs = new SecurityJwtConfigs(
                SecurityJwtConfigs.testsHardcoded().getAuthoritiesConfigs(),
                SecurityJwtConfigs.testsHardcoded().getCookiesConfigs(),
                SecurityJwtConfigs.testsHardcoded().getEssenceConfigs(),
                incidentConfigs,
                SecurityJwtConfigs.testsHardcoded().getJwtTokensConfigs(),
                SecurityJwtConfigs.testsHardcoded().getLoggingConfigs(),
                SecurityJwtConfigs.testsHardcoded().getSessionConfigs(),
                SecurityJwtConfigs.testsHardcoded().getUsersEmailsConfigs()
        );

        // Act
        securityJwtConfigs.assertProperties("securityJwtConfigs");
        securityJwtConfigs.printProperties("securityJwtConfigs");

        // Assert
        // no asserts
    }

    @Test
    void securityJwtConfigsIncidentsNoSessionRefreshedFailureTest() {
        var incidentConfigs = new IncidentsConfigs(
                new HashMap<>() {{
                    put(AUTHENTICATION_LOGIN, randomBoolean());
                    put(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD, false);
                    put(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD, true);
                    put(AUTHENTICATION_LOGOUT, randomBoolean());
                    put(AUTHENTICATION_LOGOUT_MIN, randomBoolean());
                    put(SESSION_EXPIRED, randomBoolean());
                    put(REGISTER1, randomBoolean());
                    put(REGISTER1_FAILURE, randomBoolean());
                }}
        );
        var securityJwtConfigs = new SecurityJwtConfigs(
                SecurityJwtConfigs.testsHardcoded().getAuthoritiesConfigs(),
                SecurityJwtConfigs.testsHardcoded().getCookiesConfigs(),
                SecurityJwtConfigs.testsHardcoded().getEssenceConfigs(),
                incidentConfigs,
                SecurityJwtConfigs.testsHardcoded().getJwtTokensConfigs(),
                SecurityJwtConfigs.testsHardcoded().getLoggingConfigs(),
                SecurityJwtConfigs.testsHardcoded().getSessionConfigs(),
                SecurityJwtConfigs.testsHardcoded().getUsersEmailsConfigs()
        );

        // Act
        var throwable = catchThrowable(() -> securityJwtConfigs.assertProperties("securityJwtConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Attribute `incidentsConfigs.typesConfigs` requirements: `[Authentication Login, Authentication Login Failure Username/Masked Password, Authentication Login Failure Username/Password, Authentication Logout, Authentication Logout Min, Register1, Register1 Failure, Session Expired, Session Refreshed]`, disjunction: `[Session Refreshed]`");
    }

    @Test
    void securityJwtConfigsIncidentsOnlyOneLoginFailureTest() {
        var incidentConfigs = new IncidentsConfigs(
                new HashMap<>() {{
                    put(AUTHENTICATION_LOGIN, randomBoolean());
                    put(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD, true);
                    put(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD, true);
                    put(AUTHENTICATION_LOGOUT, randomBoolean());
                    put(AUTHENTICATION_LOGOUT_MIN, randomBoolean());
                    put(SESSION_REFRESHED, randomBoolean());
                    put(SESSION_EXPIRED, randomBoolean());
                    put(REGISTER1, randomBoolean());
                    put(REGISTER1_FAILURE, randomBoolean());
                }}
        );
        var securityJwtConfigs = new SecurityJwtConfigs(
                SecurityJwtConfigs.testsHardcoded().getAuthoritiesConfigs(),
                SecurityJwtConfigs.testsHardcoded().getCookiesConfigs(),
                SecurityJwtConfigs.testsHardcoded().getEssenceConfigs(),
                incidentConfigs,
                SecurityJwtConfigs.testsHardcoded().getJwtTokensConfigs(),
                SecurityJwtConfigs.testsHardcoded().getLoggingConfigs(),
                SecurityJwtConfigs.testsHardcoded().getSessionConfigs(),
                SecurityJwtConfigs.testsHardcoded().getUsersEmailsConfigs()
        );

        // Act
        var throwable = catchThrowable(() -> securityJwtConfigs.assertProperties("securityJwtConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Please configure login failure incident feature. Only one feature type could be enabled");
    }

    @Test
    void securityJwtWebsocketsConfigsTest() {
        // Act
        SecurityJwtWebsocketsConfigs.testsHardcoded().assertProperties("securityJwtWebsocketsConfigs");
        SecurityJwtWebsocketsConfigs.testsHardcoded().printProperties("securityJwtWebsocketsConfigs");

        // Assert
        // no asserts
    }

    @Test
    void mongodbSecurityJwtConfigsTest() {
        // Act
        MongodbSecurityJwtConfigs.testsHardcoded().assertProperties("mongodbSecurityJwtConfigs");
        MongodbSecurityJwtConfigs.testsHardcoded().printProperties("mongodbSecurityJwtConfigs");

        // Assert
        // no asserts
    }
}
