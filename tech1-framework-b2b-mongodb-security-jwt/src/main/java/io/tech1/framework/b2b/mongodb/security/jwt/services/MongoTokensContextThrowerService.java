package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractTokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenDbNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class MongoTokensContextThrowerService extends AbstractTokensContextThrowerService {

    // Assistants
    private final JwtUserDetailsService jwtUserDetailsService;
    // Repositories
    private final MongoUsersSessionsRepository mongoUsersSessionsRepository;

    @Autowired
    public MongoTokensContextThrowerService(
            JwtUserDetailsService jwtUserDetailsService,
            MongoUsersSessionsRepository mongoUsersSessionsRepository,
            SecurityJwtTokenUtils securityJwtTokenUtils
    ) {
        super(
                securityJwtTokenUtils
        );
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.mongoUsersSessionsRepository = mongoUsersSessionsRepository;
    }


    @Override
    public JwtUser verifyDbPresenceOrThrow(JwtTokenValidatedClaims validatedClaims, JwtRefreshToken oldJwtRefreshToken) throws CookieRefreshTokenDbNotFoundException {
        var username = validatedClaims.safeGetUsername();
        var user = this.jwtUserDetailsService.loadUserByUsername(username.identifier());
        var databasePresence = this.mongoUsersSessionsRepository.isPresent(oldJwtRefreshToken);
        if (!databasePresence) {
            SecurityContextHolder.clearContext();
            throw new CookieRefreshTokenDbNotFoundException(username);
        }
        return user;
    }
}
