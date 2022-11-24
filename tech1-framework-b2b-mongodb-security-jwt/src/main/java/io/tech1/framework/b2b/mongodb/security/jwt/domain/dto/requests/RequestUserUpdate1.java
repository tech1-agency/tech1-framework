package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.*;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RequestUserUpdate1 {
    private String zoneId;
    private String email;
    private String name;

    public static RequestUserUpdate1 of(
            String zoneId,
            String email,
            String name
    ) {
        var instance = new RequestUserUpdate1();
        instance.zoneId = zoneId;
        instance.email = email;
        instance.name = name;
        return instance;
    }
}
