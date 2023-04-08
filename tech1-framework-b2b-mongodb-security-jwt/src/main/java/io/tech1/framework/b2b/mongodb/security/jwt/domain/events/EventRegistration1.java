package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserRegistration1;
import lombok.Data;

// Lombok
@Data
public class EventRegistration1 {
    private final RequestUserRegistration1 requestUserRegistration1;
}
