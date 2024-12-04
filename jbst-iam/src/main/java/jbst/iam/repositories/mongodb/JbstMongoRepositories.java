package jbst.iam.repositories.mongodb;

public record JbstMongoRepositories(
        MongoInvitationsRepository invitationsRepository,
        MongoUsersTokensRepository usersTokensRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
