package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.base.AbstractAuthority;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Checkbox;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.domain.properties.configs.security.jwt.*;
import io.tech1.framework.domain.utilities.enums.EnumUtility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD;
import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD;
import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.missingMappingsKeys;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
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

        // Requirements: availableAuthorities vs. defaultUsersAuthorities
        var expectedAuthorities = this.authoritiesConfigs.getAllAuthoritiesValues();
        var defaultUsersAuthorities = this.essenceConfigs.getDefaultUsers().getDefaultUsersAuthorities();
        boolean containsAll = expectedAuthorities.containsAll(defaultUsersAuthorities);
        assertTrueOrThrow(containsAll, "Please verify `defaultUsers.users.authorities`. Configuration provide unauthorized authority");

        // Requirements: availableAuthorities vs. required enum values
        var authorityClasses = this.getAbstractAuthorityClasses(this.authoritiesConfigs.getPackageName());
        int size = authorityClasses.size();
        assertTrueOrThrow(size == 1, "Please verify AbstractAuthority.class has only one sub enum. Found: `" + size + "`");
        var authorityClass = authorityClasses.iterator().next();
        Set<String> actualAuthorities = new HashSet<>();
        var abstractAuthorityClass = AbstractAuthority.class;
        var frameworkAuthorities = Stream.of(abstractAuthorityClass.getDeclaredFields())
                .map(field -> {
                    try {
                        return field.get(abstractAuthorityClass).toString();
                    } catch (IllegalAccessException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        var serverAuthorities = Stream.of(authorityClass.getEnumConstants())
                .map(AbstractAuthority::getValue)
                .collect(Collectors.toSet());
        actualAuthorities.addAll(frameworkAuthorities);
        actualAuthorities.addAll(serverAuthorities);
        assertTrueOrThrow(
                expectedAuthorities.equals(actualAuthorities),
                "Please verify AbstractAuthority sub enum configuration. Expected: `" + expectedAuthorities + "`. Actual: `" + actualAuthorities + "`"
        );
    }

    // =================================================================================================================
    // Private Method: Reflection on AbstractAuthority
    // =================================================================================================================
    @SuppressWarnings("unchecked")
    private Set<Class<? extends AbstractAuthority>> getAbstractAuthorityClasses(String packageName) {
        var beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
        var classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry, false);
        var tf = new AssignableTypeFilter(AbstractAuthority.class);
        classPathBeanDefinitionScanner.addIncludeFilter(tf);
        classPathBeanDefinitionScanner.scan(packageName);
        return Stream.of(beanDefinitionRegistry.getBeanDefinitionNames())
                .map(beanDefinitionRegistry::getBeanDefinition)
                .map(BeanDefinition::getBeanClassName)
                .map(className -> {
                    try {
                        return (Class<? extends AbstractAuthority>) Class.forName(className);
                    } catch (ClassNotFoundException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(clazz -> nonNull(clazz.getEnumConstants()))
                .collect(Collectors.toSet());
    }
}
