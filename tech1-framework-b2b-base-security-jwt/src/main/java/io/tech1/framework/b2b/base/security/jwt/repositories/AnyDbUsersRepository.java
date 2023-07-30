package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AnyDbUsersRepository {
    JwtUser loadUserByUsername(Username username) throws UsernameNotFoundException;
    JwtUser findByUsernameAsJwtUser(Username username);
    JwtUser findByEmailAsJwtUser(Email email);
    long count();
    void saveAsJwtUser(JwtUser user);
    void saveAs(RequestUserRegistration1 requestUserRegistration1, Password password, AnyDbInvitationCode invitationCode);
}
