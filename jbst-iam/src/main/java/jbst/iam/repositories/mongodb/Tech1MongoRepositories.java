package jbst.iam.repositories.mongodb;

public record Tech1MongoRepositories(
        MongoInvitationCodesRepository invitationCodeRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
