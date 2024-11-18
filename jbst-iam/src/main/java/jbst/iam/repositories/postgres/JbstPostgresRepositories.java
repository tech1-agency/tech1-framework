package jbst.iam.repositories.postgres;

public record JbstPostgresRepositories(
        PostgresInvitationsRepository invitationsRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
