package jbst.iam.domain.events;

import jbst.iam.domain.dto.requests.RequestUserRegistration1;

public record EventRegistration1(
        RequestUserRegistration1 requestUserRegistration1
) {
}
