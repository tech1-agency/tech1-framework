package io.tech1.framework.b2b.mongodb.security.jwt.essence.base;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.essence.EssenceConstructor;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_B2B_MONGODB_SECURITY_JWT_PREFIX;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseEssenceConstructor implements EssenceConstructor {

    // Repositories
    protected final InvitationCodeRepository invitationCodeRepository;
    protected final UserRepository userRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public boolean isDefaultUsersEnabled() {
        return this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers().isEnabled();
    }

    @Override
    public boolean isInvitationCodesEnabled() {
        return this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getInvitationCodes().isEnabled();
    }

    @Override
    public void addDefaultUsers() {
        assertTrueOrThrow(this.isDefaultUsersEnabled(), invalidAttribute("essenceConfigs.defaultUsers.enabled == true"));
        if (this.userRepository.count() == 0L) {
            LOGGER.warn(FRAMEWORK_B2B_MONGODB_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. No users in database. Establish database structure");
            var defaultUsers = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers().getUsers();
            var users = defaultUsers.stream().map(defaultUser -> {
                var username = defaultUser.getUsername();
                var user = new DbUser(
                        username,
                        defaultUser.getPassword(),
                        defaultUser.getZoneId().getId(),
                        defaultUser.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
                user.setEmail(defaultUser.getEmail());
                LOGGER.debug(FRAMEWORK_B2B_MONGODB_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. Convert default user. Username: {}", username);
                return user;
            }).collect(Collectors.toList());
            this.userRepository.saveAll(users);
            LOGGER.warn(FRAMEWORK_B2B_MONGODB_SECURITY_JWT_PREFIX + " Essence `defaultUsers` is completed. Saved dbRecords: `{}`", users.size());
        } else {
            LOGGER.warn(FRAMEWORK_B2B_MONGODB_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. Users are already saved in database. Please double check");
        }
    }

    @Override
    public void addDefaultUsersInvitationCodes() {
        assertTrueOrThrow(this.isInvitationCodesEnabled(), invalidAttribute("essenceConfigs.invitationCodes.enabled == true"));
        var authorities = this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();
        var simpleGrantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        var defaultUsers = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers();
        defaultUsers.getUsers().forEach(defaultUser -> {
            var username = defaultUser.getUsername();
            var invitationCodes = this.invitationCodeRepository.findByOwner(username);
            if (invitationCodes.size() == 0L) {
                LOGGER.warn(FRAMEWORK_B2B_MONGODB_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. No invitation codes in database. Username: `{}`", username);
                var dbInvitationCodes = IntStream.range(0, 10)
                        .mapToObj(i ->
                                new DbInvitationCode(
                                        username,
                                        simpleGrantedAuthorities
                                )
                        ).collect(Collectors.toList());
                this.invitationCodeRepository.saveAll(dbInvitationCodes);
            } else {
                LOGGER.warn(FRAMEWORK_B2B_MONGODB_SECURITY_JWT_PREFIX + " Essence `defaultUsers`. Invitation codes are already saved in database. Username: `{}`", username);
            }
        });
    }
}
