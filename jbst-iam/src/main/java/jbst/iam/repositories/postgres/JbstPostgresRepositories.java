package jbst.iam.repositories.postgres;

public record JbstPostgresRepositories(
        PostgresInvitationCodesRepository invitationCodeRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
