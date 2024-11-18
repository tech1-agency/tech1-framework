package jbst.iam.repositories.mongodb;

public record JbstMongoRepositories(
        MongoInvitationCodesRepository invitationCodeRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
