package jbst.iam.repositories.mongodb;

public record JbstMongoRepositories(
        MongoInvitationsRepository invitationsRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
