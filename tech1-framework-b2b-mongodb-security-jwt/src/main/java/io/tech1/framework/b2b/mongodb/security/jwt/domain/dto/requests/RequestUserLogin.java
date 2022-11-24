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
public class RequestUserLogin {
    private String username;
    private String password;
}
