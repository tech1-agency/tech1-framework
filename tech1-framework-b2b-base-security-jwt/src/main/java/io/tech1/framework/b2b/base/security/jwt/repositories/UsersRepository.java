package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.InvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UsersRepository {
    TuplePresence<JwtUser> isPresent(UserId userId);
    JwtUser loadUserByUsername(Username username) throws UsernameNotFoundException;
    JwtUser findByUsernameAsJwtUserOrNull(Username username);
    JwtUser findByEmailAsJwtUserOrNull(Email email);
    long count();
    UserId saveAs(JwtUser user);
    UserId saveAs(RequestUserRegistration1 requestUserRegistration1, Password password, InvitationCode invitationCode);
}
