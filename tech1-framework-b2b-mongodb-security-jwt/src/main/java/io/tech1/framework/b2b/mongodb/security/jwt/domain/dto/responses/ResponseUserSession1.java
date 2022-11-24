package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ResponseUserSession1 {
    private final String refreshToken;
}
