package jbst.iam.essence;

import jbst.iam.domain.mongodb.MongoDbInvitation;
import jbst.iam.domain.mongodb.MongoDbUser;
import jbst.iam.repositories.mongodb.MongoInvitationsRepository;
import jbst.iam.repositories.mongodb.MongoUsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.domain.properties.base.DefaultUser;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

public class MongoBaseEssenceConstructor extends AbstractEssenceConstructor {

    // Repositories
    protected final MongoInvitationsRepository mongoInvitationsRepository;
    protected final MongoUsersRepository mongoUsersRepository;

    public MongoBaseEssenceConstructor(
            MongoInvitationsRepository invitationCodesRepository,
            MongoUsersRepository usersRepository,
            JbstProperties jbstProperties
    ) {
        super(
                invitationCodesRepository,
                usersRepository,
                jbstProperties
        );
        this.mongoInvitationsRepository = invitationCodesRepository;
        this.mongoUsersRepository = usersRepository;
    }

    @Override
    public long saveDefaultUsers(List<DefaultUser> defaultUsers) {
        var dbUsers = defaultUsers.stream().
                map(defaultUser -> {
                    var username = defaultUser.getUsername();
                    var user = new MongoDbUser(
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
        this.mongoUsersRepository.saveAll(dbUsers);
        return dbUsers.size();
    }

    @Override
    public void saveInvitationCodes(DefaultUser defaultUser, Set<SimpleGrantedAuthority> authorities) {
        var dbInvitationCodes = IntStream.range(0, 10)
                .mapToObj(i ->
                        new MongoDbInvitation(
                                defaultUser.getUsername(),
                                authorities
                        )
                )
                .toList();
        this.mongoInvitationsRepository.saveAll(dbInvitationCodes);
    }
}
