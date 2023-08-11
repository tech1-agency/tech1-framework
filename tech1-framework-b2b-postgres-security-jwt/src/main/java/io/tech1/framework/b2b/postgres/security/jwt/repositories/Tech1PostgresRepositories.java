package io.tech1.framework.b2b.postgres.security.jwt.repositories;

public record Tech1PostgresRepositories(
        PostgresInvitationCodesRepository invitationCodeRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
