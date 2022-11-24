package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.*;

import java.util.Set;

// Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RequestNewInvitationCodeParams {
    private Set<String> authorities;
}
