package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.*;

// Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RequestUserRegistration1 {
    private String username;
    private String zoneId;
    private String password;
    private String confirmPassword;
    private String invitationCode;
}
