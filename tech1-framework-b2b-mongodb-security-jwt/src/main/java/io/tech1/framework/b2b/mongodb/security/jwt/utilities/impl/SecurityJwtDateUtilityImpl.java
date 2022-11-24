package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtDateUtility;
import io.tech1.framework.domain.properties.base.TimeAmount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtDateUtilityImpl implements SecurityJwtDateUtility {

    @Override
    public Date getIssuedAt() {
        return new Date();
    }

    @Override
    public Date getExpiration(TimeAmount timeAmount) {
        var zoneId = ZoneId.systemDefault();
        var issuedAt = LocalDateTime.now(zoneId);
        var expiration = issuedAt.plus(timeAmount.getAmount(), timeAmount.getUnit());
        return convertLocalDateTime(expiration, zoneId);
    }
}
