package jbst.iam.domain.events;

import jbst.foundation.domain.base.Username;

public record EventRegistration0Failure(
        Username username,
        String exception
) {
}
