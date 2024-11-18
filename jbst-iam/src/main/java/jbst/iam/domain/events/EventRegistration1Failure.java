package jbst.iam.domain.events;

import jbst.foundation.domain.base.Username;

public record EventRegistration1Failure(
        Username username,
        String invitation,
        Username invitationOwner,
        String exception
) {
    public static EventRegistration1Failure of(
            Username username,
            String invitation,
            String exception
    ) {
        return new EventRegistration1Failure(
                username,
                invitation,
                Username.dash(),
                exception
        );
    }
}
