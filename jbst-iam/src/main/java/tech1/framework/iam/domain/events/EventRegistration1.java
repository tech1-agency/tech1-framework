package tech1.framework.iam.domain.events;

import tech1.framework.iam.domain.dto.requests.RequestUserRegistration1;

public record EventRegistration1(
        RequestUserRegistration1 requestUserRegistration1
) {
}
