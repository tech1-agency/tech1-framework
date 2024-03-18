package io.tech1.framework.b2b.mongodb.security.jwt.essence;

import io.tech1.framework.b2b.base.security.jwt.essense.AbstractEssenceConstructor;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import io.tech1.framework.domain.properties.base.DefaultUser;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class MongoBaseEssenceConstructor extends AbstractEssenceConstructor {

    // Repositories
    protected final MongoInvitationCodesRepository mongoInvitationCodesRepository;
    protected final MongoUsersRepository mongoUsersRepository;

    @Autowired
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
