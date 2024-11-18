package tech1.framework.iam.repositories;

import tech1.framework.iam.domain.db.InvitationCode;
import tech1.framework.iam.domain.dto.requests.RequestUserRegistration1;
import tech1.framework.iam.domain.identifiers.UserId;
import tech1.framework.iam.domain.jwt.JwtUser;
import tech1.framework.foundation.domain.base.Email;
import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.tuples.TuplePresence;
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
