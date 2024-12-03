package jbst.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Password;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.iam.domain.db.Invitation;
import jbst.iam.domain.db.UserEmailDetails;
import jbst.iam.domain.dto.requests.RequestUserRegistration0;
import jbst.iam.domain.dto.requests.RequestUserRegistration1;
import jbst.iam.domain.identifiers.UserId;
import jbst.iam.domain.jwt.JwtUser;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.util.*;

import static java.util.Objects.nonNull;
import static jbst.foundation.domain.base.AbstractAuthority.*;
import static jbst.foundation.utilities.random.RandomUtility.*;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static org.springframework.util.StringUtils.capitalize;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = MongoDbUser.MONGO_TABLE_NAME)
public class MongoDbUser {
    public static final String MONGO_TABLE_NAME = "jbst_users";

    @Id
    private String id;
    private Username username;
    private Password password;
    @Schema(type = "string")
    private ZoneId zoneId;
    private Set<SimpleGrantedAuthority> authorities;
    private Email email;
    private String name;
    private boolean passwordChangeRequired;
    private UserEmailDetails emailDetails;
    private Map<String, Object> attributes;

    public MongoDbUser(
            Username username,
            Password password,
            ZoneId zoneId,
            Set<SimpleGrantedAuthority> authorities,
            boolean passwordChangeRequired,
            UserEmailDetails emailDetails
    ) {
        this.username = username;
        this.password = password;
        this.zoneId = zoneId;
        this.authorities = authorities;
        this.passwordChangeRequired = passwordChangeRequired;
        this.emailDetails = emailDetails;
        this.attributes = new HashMap<>();
    }

    public MongoDbUser(
            RequestUserRegistration0 requestUserRegistration0,
            Password password
    ) {
        this(
                requestUserRegistration0.username(),
                password,
                requestUserRegistration0.zoneId(),
                new HashSet<>(),
                false,
                UserEmailDetails.required()
        );
    }

    public MongoDbUser(
            RequestUserRegistration1 requestUserRegistration1,
            Password password,
            Invitation invitation
    ) {
        this(
                requestUserRegistration1.username(),
                password,
                requestUserRegistration1.zoneId(),
                invitation.authorities(),
                false,
                UserEmailDetails.unnecessary()
        );
    }

    public MongoDbUser(JwtUser user) {
        this.id = user.id().value();
        this.username = user.username();
        this.password = user.password();
        this.zoneId = user.zoneId();
        this.authorities = user.authorities();
        this.email = user.email();
        this.name = user.name();
        this.passwordChangeRequired = user.passwordChangeRequired();
        this.emailDetails = user.emailDetails();
        this.attributes = user.attributes();
    }

    public static MongoDbUser random(String username, String authority) {
        return random(username, Set.of(authority));
    }

    public static MongoDbUser random(String username, Set<String> authorities) {
        var user = new MongoDbUser(
                Username.of(username),
                Password.random(),
                randomZoneId(),
                getSimpleGrantedAuthorities(authorities),
                randomBoolean(),
                UserEmailDetails.random()
        );
        user.setEmail(Email.of(username + "@" + JbstConstants.Domains.HARDCODED));
        user.setName(capitalize(randomString()) + " " + capitalize(randomString()));
        user.setAttributes(
                Map.of(
                        randomString(), randomString(),
                        randomString(), randomLong()
                )
        );
        return user;
    }

    public static MongoDbUser randomSuperadmin(String username) {
        return random(username, SUPERADMIN);
    }

    public static MongoDbUser randomAdmin(String username) {
        return random(username, "admin");
    }

    public static List<MongoDbUser> dummies1() {
        return List.of(
                MongoDbUser.randomSuperadmin("sa1"),
                MongoDbUser.randomSuperadmin("sa2"),
                MongoDbUser.randomAdmin("admin1"),
                MongoDbUser.random("user1", Set.of("user", INVITATIONS_WRITE)),
                MongoDbUser.random("user2", Set.of("user", INVITATIONS_READ)),
                MongoDbUser.random("sa3", Set.of(INVITATIONS_READ, SUPERADMIN, INVITATIONS_WRITE))
        );
    }

    @JsonIgnore
    @Transient
    public Map<String, Object> getNotNullAttributes() {
        return nonNull(this.attributes) ? this.attributes : new HashMap<>();
    }

    @JsonIgnore
    @Transient
    public UserId userId() {
        return new UserId(this.id);
    }

    @JsonIgnore
    @Transient
    public JwtUser asJwtUser() {
        return new JwtUser(
                this.userId(),
                this.username,
                this.password,
                this.zoneId,
                this.authorities,
                this.email,
                this.name,
                this.passwordChangeRequired,
                this.emailDetails,
                this.attributes
        );
    }
}
