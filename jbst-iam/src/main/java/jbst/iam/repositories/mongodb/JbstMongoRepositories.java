package jbst.iam.repositories.mongodb;

public record JbstMongoRepositories(
        MongoInvitationsRepository invitationCodeRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
