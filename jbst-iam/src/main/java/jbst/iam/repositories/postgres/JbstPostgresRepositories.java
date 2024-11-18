package jbst.iam.repositories.postgres;

public record JbstPostgresRepositories(
        PostgresInvitationsRepository invitationCodeRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
