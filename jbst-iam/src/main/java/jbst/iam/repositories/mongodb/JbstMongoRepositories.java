package jbst.iam.repositories.mongodb;

public record JbstMongoRepositories(
        MongoInvitationsRepository invitationsRepository,
        MongoUsersEmailsTokensRepository usersEmailsTokensRepository,
        MongoUsersRepository userRepository,
        MongoUsersSessionsRepository userSessionRepository
) {
}
