package jbst.iam.essence;

import jbst.iam.domain.mongodb.MongoDbInvitationCode;
import jbst.iam.domain.mongodb.MongoDbUser;
import jbst.iam.repositories.mongodb.MongoInvitationCodesRepository;
import jbst.iam.repositories.mongodb.MongoUsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.domain.properties.base.DefaultUser;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

public class MongoBaseEssenceConstructor extends AbstractEssenceConstructor {

    // Repositories
    protected final MongoInvitationCodesRepository mongoInvitationCodesRepository;
    protected final MongoUsersRepository mongoUsersRepository;

    public MongoBaseEssenceConstructor(
            MongoInvitationCodesRepository invitationCodesRepository,
            MongoUsersRepository usersRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                invitationCodesRepository,
                usersRepository,
                applicationFrameworkProperties
        );
        this.mongoInvitationCodesRepository = invitationCodesRepository;
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
                        new MongoDbInvitationCode(
                                defaultUser.getUsername(),
                                authorities
                        )
                )
                .toList();
        this.mongoInvitationCodesRepository.saveAll(dbInvitationCodes);
    }
}
