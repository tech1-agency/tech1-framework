package tech1.framework.iam.services.postgres;

import tech1.framework.iam.services.abstracts.AbstractTokensContextThrowerService;
import tech1.framework.iam.utils.SecurityJwtTokenUtils;
import tech1.framework.iam.assistants.userdetails.PostgresUserDetailsAssistant;
import tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostgresTokensContextThrowerService extends AbstractTokensContextThrowerService {

    @Autowired
    public PostgresTokensContextThrowerService(
            PostgresUserDetailsAssistant userDetailsAssistant,
            PostgresUsersSessionsRepository usersSessionsRepository,
            SecurityJwtTokenUtils securityJwtTokenUtils
    ) {
        super(
                userDetailsAssistant,
                usersSessionsRepository,
                securityJwtTokenUtils
        );
    }
}
