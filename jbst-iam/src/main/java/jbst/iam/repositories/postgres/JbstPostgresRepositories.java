package jbst.iam.repositories.postgres;

public record JbstPostgresRepositories(
        PostgresInvitationsRepository invitationsRepository,
        PostgresUsersTokensRepository usersTokensRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
