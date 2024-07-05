package io.tech1.framework.iam.repositories.mongodb;

public record Tech1MongoRepositories(
        MongoInvitationCodesRepository invitationCodeRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
