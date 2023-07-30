package io.tech1.framework.b2b.base.security.jwt.essense;

import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_B2B_SECURITY_JWT_PREFIX;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
public abstract class AbstractEssenceConstructor implements EssenceConstructor {

    // Repositories
    protected final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    protected final AnyDbUsersRepository anyDbUsersRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    protected AbstractEssenceConstructor(
            AnyDbInvitationCodesRepository anyDbInvitationCodesRepository,
            AnyDbUsersRepository anyDbUsersRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        this.anyDbInvitationCodesRepository = anyDbInvitationCodesRepository;
        this.anyDbUsersRepository = anyDbUsersRepository;
        this.applicationFrameworkProperties = applicationFrameworkProperties;
    }

    public void addDefaultUsers() {
        var essenceConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs();
        assertTrueOrThrow(
                essenceConfigs.getDefaultUsers().isEnabled(),
                invalidAttribute("essenceConfigs.defaultUsers.enabled == true")
        );
        if (this.anyDbUsersRepository.count() == 0L) {
            LOGGER.warn(FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. No users in database. Establish database structure");
            var usersCount = this.saveDefaultUsers(essenceConfigs.getDefaultUsers().getUsers());
            LOGGER.warn(FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Essence `defaultUsers` is completed. Saved dbRecords: `{}`", usersCount);
        } else {
            LOGGER.warn(FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. Users are already saved in database. Please double check");
        }
    }

    public void addDefaultUsersInvitationCodes() {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        var essenceConfigs = securityJwtConfigs.getEssenceConfigs();
        assertTrueOrThrow(
                essenceConfigs.getInvitationCodes().isEnabled(),
                invalidAttribute("essenceConfigs.invitationCodes.enabled == true")
        );
        var authorities = securityJwtConfigs.getAuthoritiesConfigs().getAvailableAuthorities();
        var simpleGrantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        essenceConfigs.getDefaultUsers().getUsers().forEach(defaultUser -> {
            var username = defaultUser.getUsername();
            if (this.anyDbInvitationCodesRepository.countByOwner(username) == 0L) {
                LOGGER.warn(FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. No invitation codes in database. Username: `{}`", username);
                this.saveInvitationCodes(defaultUser, simpleGrantedAuthorities);
            } else {
                LOGGER.warn(FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. Invitation codes are already saved in database. Username: `{}`", username);
            }
        });
    }
}
