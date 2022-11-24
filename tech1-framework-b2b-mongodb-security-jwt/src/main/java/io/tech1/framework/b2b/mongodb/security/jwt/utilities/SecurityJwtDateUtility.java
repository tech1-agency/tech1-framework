package io.tech1.framework.b2b.mongodb.security.jwt.utilities;

import io.tech1.framework.domain.properties.base.TimeAmount;

import java.util.Date;

public interface SecurityJwtDateUtility {
    Date getIssuedAt();
    Date getExpiration(TimeAmount timeAmount);
}
