package jbst.iam.services.mongodb;

import jbst.iam.assistants.userdetails.MongoUserDetailsAssistant;
import jbst.iam.repositories.mongodb.MongoUsersSessionsRepository;
import jbst.iam.services.abstracts.AbstractTokensContextThrowerService;
import jbst.iam.utils.SecurityJwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoTokensContextThrowerService extends AbstractTokensContextThrowerService {

    @Autowired
    public MongoTokensContextThrowerService(
            MongoUserDetailsAssistant userDetailsAssistant,
            MongoUsersSessionsRepository usersSessionsRepository,
            SecurityJwtTokenUtils securityJwtTokenUtils
    ) {
        super(
                userDetailsAssistant,
                usersSessionsRepository,
                securityJwtTokenUtils
        );
    }
}
