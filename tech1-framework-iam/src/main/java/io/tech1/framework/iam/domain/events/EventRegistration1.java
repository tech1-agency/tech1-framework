package io.tech1.framework.iam.domain.events;

import io.tech1.framework.iam.domain.dto.requests.RequestUserRegistration1;

public record EventRegistration1(
        RequestUserRegistration1 requestUserRegistration1
) {
}
