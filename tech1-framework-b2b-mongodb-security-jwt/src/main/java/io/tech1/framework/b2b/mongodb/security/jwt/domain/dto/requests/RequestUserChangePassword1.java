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
public class RequestUserChangePassword1 {
    private String newPassword;
    private String confirmPassword;
}
