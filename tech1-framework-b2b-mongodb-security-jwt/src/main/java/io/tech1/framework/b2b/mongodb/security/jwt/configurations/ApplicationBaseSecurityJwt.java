package io.tech1.framework.b2b.mongodb.security.jwt.configurations;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails.JwtUserDetailsAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.essence.EssenceConstructor;
import io.tech1.framework.b2b.mongodb.security.jwt.filters.JwtTokensFilter;
import io.tech1.framework.b2b.mongodb.security.jwt.handlers.exceptions.JwtAccessDeniedExceptionHandler;
import io.tech1.framework.b2b.mongodb.security.jwt.handlers.exceptions.JwtAuthenticationEntryPointExceptionHandler;
import io.tech1.framework.configurations.jasypt.ApplicationJasypt;
import io.tech1.framework.configurations.server.ApplicationSpringBootServer;
import io.tech1.framework.domain.base.AbstractAuthority;
import io.tech1.framework.emails.configurations.ApplicationEmails;
import io.tech1.framework.incidents.configurations.ApplicationIncidents;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.*;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpMethod.*;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails",
        "io.tech1.framework.b2b.mongodb.security.jwt.cookies",
        "io.tech1.framework.b2b.mongodb.security.jwt.events",
        "io.tech1.framework.b2b.mongodb.security.jwt.filters",
        "io.tech1.framework.b2b.mongodb.security.jwt.handlers.exceptions",
        "io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters",
        "io.tech1.framework.b2b.mongodb.security.jwt.resources",
        "io.tech1.framework.b2b.mongodb.security.jwt.services",
        "io.tech1.framework.b2b.mongodb.security.jwt.sessions",
        "io.tech1.framework.b2b.mongodb.security.jwt.utilities",
        "io.tech1.framework.b2b.mongodb.security.jwt.validators",
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.utilities.geo"
        // -------------------------------------------------------------------------------------------------------------
})
@EnableWebSecurity
@Import({
        ApplicationSpringBootServer.class,
        ApplicationJasypt.class,
        ApplicationSecurityJwtMvc.class,
        ApplicationIncidents.class,
        ApplicationEmails.class,
        ApplicationMongodb.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationBaseSecurityJwt extends WebSecurityConfigurerAdapter {

    // Assistants
    private final JwtUserDetailsAssistant jwtUserDetailsAssistant;
    // Essence
    private final EssenceConstructor essenceConstructor;
    // Filters
    private final JwtTokensFilter jwtTokensFilter;
    // Handlers
    private final JwtAuthenticationEntryPointExceptionHandler jwtAuthenticationEntryPointExceptionHandler;
    private final JwtAccessDeniedExceptionHandler jwtAccessDeniedExceptionHandler;
    // Configurer
    private final AbstractApplicationSecurityJwtConfigurer abstractApplicationSecurityJwtConfigurer;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        assertProperties(securityJwtConfigs, "securityJwtConfigs");

        // Requirements: availableAuthorities vs. defaultUsersAuthorities
        var authoritiesConfigs = securityJwtConfigs.getAuthoritiesConfigs();
        var expectedAuthorities = authoritiesConfigs.getAllAuthoritiesValues();
        var defaultUsersAuthorities = securityJwtConfigs.getEssenceConfigs().getDefaultUsers().getDefaultUsersAuthorities();
        boolean containsAll = expectedAuthorities.containsAll(defaultUsersAuthorities);
        assertTrueOrThrow(containsAll, "Please verify `defaultUsers.users.authorities`. Configuration provide unauthorized authority");

        // Requirements: availableAuthorities vs. required enum values
        var authorityClasses = this.getAbstractAuthorityClasses(authoritiesConfigs.getPackageName());
        int size = authorityClasses.size();
        assertTrueOrThrow(size == 1, "Please verify AbstractAuthority.class has only one sub enum. Found: `" + size + "`");
        var authorityClass = authorityClasses.iterator().next();
        Set<String> actualAuthorities = new HashSet<>();
        var abstractAuthorityClass = AbstractAuthority.class;
        var frameworkAuthorities = Stream.of(abstractAuthorityClass.getDeclaredFields())
                .map(field -> {
                    try {
                        return field.get(abstractAuthorityClass).toString();
                    } catch (IllegalAccessException e) {
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

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.jwtUserDetailsAssistant)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        this.abstractApplicationSecurityJwtConfigurer.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var basePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();
        http.cors()
                .and()
                    .csrf()
                        .disable()
                    .addFilterBefore(
                            this.jwtTokensFilter,
                            UsernamePasswordAuthenticationFilter.class
                    )
                    .exceptionHandling()
                        .authenticationEntryPoint(this.jwtAuthenticationEntryPointExceptionHandler)
                        .accessDeniedHandler(this.jwtAccessDeniedExceptionHandler)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        var urlRegistry = http.authorizeRequests();
        urlRegistry.antMatchers(POST, basePathPrefix + "/authentication/login").anonymous();
        urlRegistry.antMatchers(POST, basePathPrefix +"/authentication/logout").permitAll();
        urlRegistry.antMatchers(POST, basePathPrefix + "/authentication/refreshToken").permitAll();
        urlRegistry.antMatchers(GET, basePathPrefix + "/session/current").authenticated();
        urlRegistry.antMatchers(POST, basePathPrefix + "/registration/register1").denyAll();
        urlRegistry.antMatchers(POST, basePathPrefix + "/user/update1").denyAll();
        urlRegistry.antMatchers(POST, basePathPrefix + "/user/update2").authenticated();
        urlRegistry.antMatchers(POST, basePathPrefix + "/user/changePassword1").denyAll();

        if (this.essenceConstructor.isInvitationCodesEnabled()) {
            urlRegistry.antMatchers(GET, basePathPrefix + "/invitationCode").hasAuthority(INVITATION_CODE_READ);
            urlRegistry.antMatchers(POST, basePathPrefix + "/invitationCode").hasAuthority(INVITATION_CODE_WRITE);
            urlRegistry.antMatchers(DELETE, basePathPrefix + "/invitationCode/{invitationCodeId}").hasAuthority(INVITATION_CODE_WRITE);
        } else {
            urlRegistry.antMatchers( basePathPrefix + "/invitationCode/**").denyAll();
        }
        urlRegistry.antMatchers(basePathPrefix + "/hardware/**").authenticated();
        urlRegistry.antMatchers(basePathPrefix + "/superadmin/**").hasAuthority(SUPER_ADMIN);
        urlRegistry.antMatchers(basePathPrefix + "/**").authenticated();

        urlRegistry.antMatchers("/actuator/**").permitAll();

        this.abstractApplicationSecurityJwtConfigurer.configure(http);

        urlRegistry.anyRequest().authenticated();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
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
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(clazz -> nonNull(clazz.getEnumConstants()))
                .collect(Collectors.toSet());
    }
}
