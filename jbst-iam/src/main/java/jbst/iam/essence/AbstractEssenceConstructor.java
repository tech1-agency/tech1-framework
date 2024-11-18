package jbst.iam.essence;

import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.iam.repositories.InvitationsRepository;
import jbst.iam.repositories.UsersRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static jbst.foundation.domain.asserts.Asserts.assertTrueOrThrow;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractEssenceConstructor implements EssenceConstructor {

    // Repositories
    protected final InvitationsRepository invitationsRepository;
    protected final UsersRepository usersRepository;
    // Properties
    protected final JbstProperties jbstProperties;

    public void addDefaultUsers() {
        var essenceConfigs = this.jbstProperties.getSecurityJwtConfigs().getEssenceConfigs();
        assertTrueOrThrow(
                essenceConfigs.getDefaultUsers().isEnabled(),
                invalidAttribute("essenceConfigs.defaultUsers.enabled == true")
        );
        if (this.usersRepository.count() == 0L) {
            LOGGER.info(JbstConstants.Logs.PREFIX + " Essence feature 'default-users'. No users in database. Establish database structure");
            var usersCount = this.saveDefaultUsers(essenceConfigs.getDefaultUsers().getUsers());
            LOGGER.info(JbstConstants.Logs.PREFIX + " Essence feature 'default-users' is completed. Saved dbRecords: `{}`", usersCount);
        } else {
            LOGGER.info(JbstConstants.Logs.PREFIX + " Essence feature 'default-users'. Users are already saved in database. Please double check");
        }
    }

    public void addDefaultUsersInvitationCodes() {
        var securityJwtConfigs = this.jbstProperties.getSecurityJwtConfigs();
        var essenceConfigs = securityJwtConfigs.getEssenceConfigs();
        assertTrueOrThrow(
                essenceConfigs.getInvitations().isEnabled(),
                invalidAttribute("essenceConfigs.invitations.enabled == true")
        );
        var authorities = getSimpleGrantedAuthorities(securityJwtConfigs.getAuthoritiesConfigs().getAvailableAuthorities());
        essenceConfigs.getDefaultUsers().getUsers().forEach(defaultUser -> {
            var username = defaultUser.getUsername();
            if (this.invitationsRepository.countByOwner(username) == 0L) {
                LOGGER.info(JbstConstants.Logs.PREFIX + " Essence feature 'default-users'. No invitation codes in database. Username: `{}`", username);
                this.saveInvitationCodes(defaultUser, authorities);
            } else {
                LOGGER.info(JbstConstants.Logs.PREFIX + " Essence feature 'default-users'. Invitation codes are already saved in database. Username: `{}`", username);
            }
        });
    }
}
