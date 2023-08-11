package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

public record Tech1MongoRepositories(
        MongoInvitationCodesRepository invitationCodeRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
