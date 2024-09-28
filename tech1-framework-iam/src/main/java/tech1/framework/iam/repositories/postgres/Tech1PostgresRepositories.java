package tech1.framework.iam.repositories.postgres;

public record Tech1PostgresRepositories(
        PostgresInvitationCodesRepository invitationCodeRepository,
        PostgresUsersRepository userRepository,
        PostgresUsersSessionsRepository userSessionRepository
) {
}
