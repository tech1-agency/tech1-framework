package io.tech1.framework.b2b.base.security.jwt.essense;

import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_B2B_SECURITY_JWT_PREFIX;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractEssenceConstructor implements EssenceConstructor {

    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    protected final UsersRepository usersRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    public void addDefaultUsers() {
        var essenceConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs();
        assertTrueOrThrow(
                essenceConfigs.getDefaultUsers().isEnabled(),
                invalidAttribute("essenceConfigs.defaultUsers.enabled == true")
        );
        if (this.usersRepository.count() == 0L) {
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
        var authorities = getSimpleGrantedAuthorities(securityJwtConfigs.getAuthoritiesConfigs().getAvailableAuthorities());
        essenceConfigs.getDefaultUsers().getUsers().forEach(defaultUser -> {
            var username = defaultUser.getUsername();
            if (this.invitationCodesRepository.countByOwner(username) == 0L) {
                LOGGER.warn(FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. No invitation codes in database. Username: `{}`", username);
                this.saveInvitationCodes(defaultUser, authorities);
            } else {
                LOGGER.warn(FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. Invitation codes are already saved in database. Username: `{}`", username);
            }
        });
    }
}
