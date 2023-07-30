package io.tech1.framework.b2b.postgres.security.jwt.essence;

import io.tech1.framework.b2b.base.security.jwt.essense.AbstractEssenceConstructor;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.domain.properties.base.DefaultUser;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class PostgresBaseEssenceConstructor extends AbstractEssenceConstructor {

    // Repositories
    protected final PostgresInvitationCodesRepository invitationCodesRepository;
    protected final PostgresUsersRepository usersRepository;

    @Autowired
    public PostgresBaseEssenceConstructor(
            ApplicationFrameworkProperties applicationFrameworkProperties,
            PostgresInvitationCodesRepository invitationCodesRepository,
            PostgresUsersRepository usersRepository
    ) {
        super(
                applicationFrameworkProperties
        );
        this.invitationCodesRepository = invitationCodesRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean noDefaultUsers() {
        return this.usersRepository.count() == 0L;
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
                            defaultUser.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );
                    user.setEmail(defaultUser.getEmail());
                    return user;
                })
                .collect(Collectors.toList());
        this.usersRepository.saveAll(dbUsers);
        return dbUsers.size();
    }

    @Override
    public boolean noInvitationCodes(DefaultUser defaultUser) {
        return this.invitationCodesRepository.countByOwner(defaultUser.getUsername()) == 0L;
    }

    @Override
    public void saveInvitationCodes(DefaultUser defaultUser, List<SimpleGrantedAuthority> authorities) {
        var dbInvitationCodes = IntStream.range(0, 10)
                .mapToObj(i ->
                        new PostgresDbInvitationCode(
                                defaultUser.getUsername(),
                                authorities
                        )
                ).collect(Collectors.toList());
        this.invitationCodesRepository.saveAll(dbInvitationCodes);
    }
}
