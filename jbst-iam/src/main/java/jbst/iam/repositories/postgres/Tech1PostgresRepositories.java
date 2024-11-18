package jbst.iam.repositories.postgres;

public record Tech1PostgresRepositories(
        PostgresInvitationCodesRepository invitationCodeRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
