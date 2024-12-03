package jbst.iam.repositories.postgres;

public record JbstPostgresRepositories(
        PostgresInvitationsRepository invitationsRepository,
        PostgresUsersEmailsTokensRepository usersEmailsTokensRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
