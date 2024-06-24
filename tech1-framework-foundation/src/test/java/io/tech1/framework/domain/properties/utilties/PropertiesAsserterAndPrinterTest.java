package io.tech1.framework.domain.properties.utilties;

import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.base.ScheduledJob;
import io.tech1.framework.domain.properties.base.SchedulerConfiguration;
import io.tech1.framework.domain.properties.base.SpringLogging;
import io.tech1.framework.domain.properties.base.SpringServer;
import io.tech1.framework.domain.properties.configs.*;
import io.tech1.framework.domain.properties.configs.security.jwt.IncidentsConfigs;
import io.tech1.framework.domain.tests.classes.NotUsedPropertiesConfigs;
import io.tech1.framework.foundation.utilities.collections.CollectorUtility;
import io.tech1.framework.foundation.utilities.enums.EnumUtility;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomBoolean;
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
        notUsedPropertiesConfigs.assertProperties(new PropertyId("notUsedPropertiesConfigs"));
        notUsedPropertiesConfigs.printProperties(new PropertyId("notUsedPropertiesConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void serverConfigsTest() {
        // Act
        ServerConfigs.testsHardcoded().assertProperties(new PropertyId("serverConfigs"));
        ServerConfigs.testsHardcoded().printProperties(new PropertyId("serverConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void utilitiesConfigsTest() {
        // Act
        UtilitiesConfigs.testsHardcoded().assertProperties(new PropertyId("utilitiesConfigs"));
        UtilitiesConfigs.testsHardcoded().printProperties(new PropertyId("utilitiesConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void asyncConfigsTest() {
        // Act
        AsyncConfigs.testsHardcoded().assertProperties(new PropertyId("asyncConfigs"));
        AsyncConfigs.testsHardcoded().printProperties(new PropertyId("asyncConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void eventsConfigsTest() {
        // Act
        EventsConfigs.testsHardcoded().assertProperties(new PropertyId("eventsConfigs"));
        EventsConfigs.testsHardcoded().printProperties(new PropertyId("eventsConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void mvcConfigsDisabledTest() {
        // Arrange
        var mvcConfigs = new MvcConfigs(false, null, null);

        // Act
        mvcConfigs.assertProperties(new PropertyId("mvcConfigs"));
        mvcConfigs.printProperties(new PropertyId("mvcConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void mvcConfigsTest() {
        // Act
        MvcConfigs.testsHardcoded().assertProperties(new PropertyId("mvcConfigs"));
        MvcConfigs.testsHardcoded().printProperties(new PropertyId("mvcConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void emailConfigsDisabledTest() {
        // Act
        EmailConfigs.disabled().assertProperties(new PropertyId("emailConfigs"));
        EmailConfigs.disabled().printProperties(new PropertyId("emailConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void emailConfigsTest() {
        // Act
        EmailConfigs.testsHardcoded().assertProperties(new PropertyId("emailConfigs"));
        EmailConfigs.testsHardcoded().printProperties(new PropertyId("emailConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void incidentConfigsTest() {
        // Act
        IncidentConfigs.testsHardcoded().assertProperties(new PropertyId("incidentConfigs"));
        IncidentConfigs.testsHardcoded().printProperties(new PropertyId("incidentConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void hardwareMonitoringConfigsDisabledTest() {
        // Arrange
        var hardwareMonitoringConfigs = HardwareMonitoringConfigs.disabled();

        // Act
        HardwareMonitoringConfigs.disabled().assertProperties(new PropertyId("hardwareMonitoringConfigs"));

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
        var throwable = catchThrowable(() -> hardwareMonitoringConfigs.assertProperties(new PropertyId("hardwareMonitoringConfigs")));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Property \"\u001B[31mhardwareMonitoringConfigs.thresholdsConfigs\u001B[0m\" is invalid. Options: \"[CPU, HEAP, SERVER, SWAP, VIRTUAL]\". Required: \"[CPU, HEAP]\". Disjunction: \"[\u001B[31mSERVER, SWAP, VIRTUAL\u001B[0m]\"");
    }

    @Test
    void hardwareMonitoringConfigsTest() {
        // Act
        HardwareMonitoringConfigs.testsHardcoded().assertProperties(new PropertyId("hardwareMonitoringConfigs"));
        HardwareMonitoringConfigs.testsHardcoded().printProperties(new PropertyId("hardwareMonitoringConfigs"));

        // Assert
        var thresholdsConfigs = HardwareMonitoringConfigs.testsHardcoded().getThresholdsConfigs();
        assertThat(thresholdsConfigs).hasSize(5);
        assertThat(thresholdsConfigs.keySet()).isEqualTo(EnumUtility.set(HardwareName.class));
        assertThat(new HashSet<>(thresholdsConfigs.values())).hasSize(5);
    }

    @Test
    void hardwareServerConfigsTest() {
        // Act
        HardwareServerConfigs.testsHardcoded().assertProperties(new PropertyId("hardwareServerConfigs"));
        HardwareServerConfigs.testsHardcoded().printProperties(new PropertyId("hardwareServerConfigs"));

        // Assert
        // no asserts
    }

//    @RepeatedTest(5)
    @Test
    void securityJwtConfigsDisabledUsersEmailsConfigsTest() {
        // Act
        var securityJwtConfigs = SecurityJwtConfigs.disabledUsersEmailsConfigs();

        // Act
        var throwable = catchThrowable(() -> securityJwtConfigs.assertProperties(new PropertyId("securityJwtConfigs")));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).startsWith("Property");
        assertThat(throwable.getMessage()).endsWith(" is invalid");
    }

    @Test
    void securityJwtConfigsTest() {
        // Act
        SecurityJwtConfigs.testsHardcoded().assertProperties(new PropertyId("securityJwtConfigs"));
        SecurityJwtConfigs.testsHardcoded().printProperties(new PropertyId("securityJwtConfigs"));

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
        securityJwtConfigs.assertProperties(new PropertyId("securityJwtConfigs"));
        securityJwtConfigs.printProperties(new PropertyId("securityJwtConfigs"));

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
        var throwable = catchThrowable(() -> securityJwtConfigs.assertProperties(new PropertyId("securityJwtConfigs")));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Property \"\u001B[31msecurityJwtConfigs.incidentsConfigs.typesConfigs\u001B[0m\" is invalid. Options: \"[Authentication Login, Authentication Login Failure Username/Masked Password, Authentication Login Failure Username/Password, Authentication Logout, Authentication Logout Min, Register1, Register1 Failure, Session Expired, Session Refreshed]\". Required: \"[Authentication Login, Authentication Login Failure Username/Masked Password, Authentication Login Failure Username/Password, Authentication Logout, Authentication Logout Min, Register1, Register1 Failure, Session Expired]\". Disjunction: \"[\u001B[31mSession Refreshed\u001B[0m]\"");
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
        var throwable = catchThrowable(() -> securityJwtConfigs.assertProperties(new PropertyId("securityJwtConfigs")));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Please configure login failure incident feature. Only one feature type could be enabled");
    }

    @Test
    void securityJwtWebsocketsConfigsTest() {
        // Act
        SecurityJwtWebsocketsConfigs.testsHardcoded().assertProperties(new PropertyId("securityJwtWebsocketsConfigs"));
        SecurityJwtWebsocketsConfigs.testsHardcoded().printProperties(new PropertyId("securityJwtWebsocketsConfigs"));

        // Assert
        // no asserts
    }

    @Test
    void mongodbSecurityJwtConfigsTest() {
        // Act
        MongodbSecurityJwtConfigs.testsHardcoded().assertProperties(new PropertyId("mongodbSecurityJwtConfigs"));
        MongodbSecurityJwtConfigs.testsHardcoded().printProperties(new PropertyId("mongodbSecurityJwtConfigs"));

        // Assert
        // no asserts
    }
}
