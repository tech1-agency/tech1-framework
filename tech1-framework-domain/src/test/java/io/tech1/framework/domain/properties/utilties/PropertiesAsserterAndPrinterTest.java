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
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printProperties;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class PropertiesAsserterAndPrinterTest {

    @Test
    void notUsedPropertiesConfigsTest() {
        // Arrange
        var notUsedPropertiesConfigs = NotUsedPropertiesConfigs.of(
                new ScheduledJob(true, SchedulerConfiguration.testsHardcoded()),
                new SpringServer(8080),
                new SpringLogging("logback-test.xml")
        );

        // Act
        assertProperties(notUsedPropertiesConfigs, "notUsedPropertiesConfigs");
        printProperties(notUsedPropertiesConfigs);

        // Assert
        // no asserts
    }

    @Test
    void serverConfigsTest() {
        // Act
        assertProperties(ServerConfigs.testsHardcoded(), "serverConfigs");
        printProperties(ServerConfigs.testsHardcoded());

        // Assert
        // no asserts
    }

    @Test
    void utilitiesConfigsTest() {
        // Act
        assertProperties(UtilitiesConfigs.testsHardcoded(), "utilitiesConfigs");
        printProperties(UtilitiesConfigs.testsHardcoded());

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
        assertProperties(EventsConfigs.testsHardcoded(), "eventsConfigs");
        printProperties(EventsConfigs.testsHardcoded());

        // Assert
        // no asserts
    }

    @Test
    void mvcConfigsDisabledTest() {
        // Arrange
        var mvcConfigs = new MvcConfigs(
                false,
                null,
                null
        );

        // Act
        assertProperties(mvcConfigs, "mvcConfigs");
        printProperties(mvcConfigs);

        // Assert
        // no asserts
    }

    @Test
    void mvcConfigsTest() {
        // Act
        assertProperties(MvcConfigs.testsHardcoded(), "mvcConfigs");
        printProperties(MvcConfigs.testsHardcoded());

        // Assert
        // no asserts
    }

    @Test
    void emailConfigsDisabledTest() {
        // Arrange
        var emailConfigs = EmailConfigs.disabled();

        // Act
        emailConfigs.assertProperties("emailConfigs");
        emailConfigs.printProperties("emailConfigs");

        // Assert
        // no asserts
    }

    @Test
    void emailConfigsTest() {
        // Arrange
        var emailConfigs = EmailConfigs.testsHardcoded();

        // Act
        emailConfigs.assertProperties("emailConfigs");
        emailConfigs.printProperties("emailConfigs");

        // Assert
        // no asserts
    }

    @Test
    void incidentConfigsTest() {
        // Act
        assertProperties(IncidentConfigs.testsHardcoded(), "incidentConfigs");
        printProperties(IncidentConfigs.testsHardcoded());

        // Assert
        // no asserts
    }

    @Test
    void hardwareMonitoringConfigsDisabledTest() {
        // Arrange
        var hardwareMonitoringConfigs = HardwareMonitoringConfigs.disabled();

        // Act
        assertProperties(hardwareMonitoringConfigs, "hardwareMonitoringConfigs");

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
        var throwable = catchThrowable(() -> assertProperties(hardwareMonitoringConfigs, "hardwareMonitoringConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Attribute `hardwareMonitoringConfigs.thresholdsConfigs` requirements: `[CPU, HEAP, SERVER, SWAP, VIRTUAL]`, disjunction: `[SERVER, SWAP, VIRTUAL]`");
    }

    @Test
    void hardwareMonitoringConfigsTest() {
        // Act
        assertProperties(HardwareMonitoringConfigs.testsHardcoded(), "hardwareMonitoringConfigs");
        printProperties(HardwareMonitoringConfigs.testsHardcoded());

        // Assert
        var thresholdsConfigs = HardwareMonitoringConfigs.testsHardcoded().getThresholdsConfigs();
        assertThat(thresholdsConfigs).hasSize(5);
        assertThat(thresholdsConfigs.keySet()).isEqualTo(EnumUtility.set(HardwareName.class));
        assertThat(new HashSet<>(thresholdsConfigs.values())).hasSize(5);
    }

    @Test
    void hardwareServerConfigsTest() {
        // Act
        assertProperties(HardwareServerConfigs.testsHardcoded(), "hardwareServerConfigs");
        printProperties(HardwareServerConfigs.testsHardcoded());

        // Assert
        // no asserts
    }

    @RepeatedTest(5)
    void securityJwtConfigsDisabledUsersEmailsConfigsTest() {
        // Act
        var securityJwtConfigs = SecurityJwtConfigs.disabledUsersEmailsConfigs();

        // Act
        var throwable = catchThrowable(() -> assertProperties(securityJwtConfigs, "securityJwtConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).startsWith("Attribute `securityJwtConfigs.");
        assertThat(throwable.getMessage()).endsWith("` is invalid");
    }

    @Test
    void securityJwtConfigsTest() {
        // Act
        assertProperties(SecurityJwtConfigs.testsHardcoded(), "securityJwtConfigs");
        printProperties(SecurityJwtConfigs.testsHardcoded());

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
        assertProperties(securityJwtConfigs, "securityJwtConfigs");
        printProperties(securityJwtConfigs);

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
        var throwable = catchThrowable(() -> assertProperties(securityJwtConfigs, "securityJwtConfigs"));

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
        var throwable = catchThrowable(() -> assertProperties(securityJwtConfigs, "securityJwtConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Please configure login failure incident feature. Only one feature type could be enabled");
    }

    @Test
    void securityJwtWebsocketsConfigsTest() {
        // Act
        assertProperties(SecurityJwtWebsocketsConfigs.testsHardcoded(), "securityJwtWebsocketsConfigs");
        printProperties(SecurityJwtWebsocketsConfigs.testsHardcoded());

        // Assert
        // no asserts
    }

    @Test
    void mongodbSecurityJwtConfigsTest() {
        // Act
        assertProperties(MongodbSecurityJwtConfigs.testsHardcoded(), "mongodbSecurityJwtConfigs");
        printProperties(MongodbSecurityJwtConfigs.testsHardcoded());

        // Assert
        // no asserts
    }
}
