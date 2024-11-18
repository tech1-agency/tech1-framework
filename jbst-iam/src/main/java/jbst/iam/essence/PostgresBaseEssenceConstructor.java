package jbst.iam.essence;

import jbst.iam.domain.postgres.db.PostgresDbInvitationCode;
import jbst.iam.domain.postgres.db.PostgresDbUser;
import jbst.iam.repositories.postgres.PostgresInvitationCodesRepository;
import jbst.iam.repositories.postgres.PostgresUsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.domain.properties.base.DefaultUser;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

public class PostgresBaseEssenceConstructor extends AbstractEssenceConstructor {

    // Repositories
    protected final PostgresInvitationCodesRepository postgresInvitationCodesRepository;
    protected final PostgresUsersRepository postgresUsersRepository;

    public PostgresBaseEssenceConstructor(
            PostgresInvitationCodesRepository invitationCodesRepository,
            PostgresUsersRepository usersRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                invitationCodesRepository,
                usersRepository,
                applicationFrameworkProperties
        );
        this.postgresInvitationCodesRepository = invitationCodesRepository;
        this.postgresUsersRepository = usersRepository;
    }

    @Override
    public long saveDefaultUsers(List<DefaultUser> defaultUsers) {
        var dbUsers = defaultUsers.stream().
                map(defaultUser -> {
                    var username = defaultUser.getUsername();
                    var user = new PostgresDbUser(
                            username,
                            defaultUser.getPassword(),
                            defaultUser.getZoneId(),
                            getSimpleGrantedAuthorities(defaultUser.getAuthorities()),
                            defaultUser.isPasswordChangeRequired()
                    );
                    user.setEmail(defaultUser.getEmailOrNull());
                    return user;
                })
                .toList();
        this.postgresUsersRepository.saveAll(dbUsers);
        return dbUsers.size();
    }

    @Override
    public void saveInvitationCodes(DefaultUser defaultUser, Set<SimpleGrantedAuthority> authorities) {
        var dbInvitationCodes = IntStream.range(0, 10)
                .mapToObj(i ->
                        new PostgresDbInvitationCode(
                                defaultUser.getUsername(),
                                authorities
                        )
                )
                .toList();
        this.postgresInvitationCodesRepository.saveAll(dbInvitationCodes);
    }
}
