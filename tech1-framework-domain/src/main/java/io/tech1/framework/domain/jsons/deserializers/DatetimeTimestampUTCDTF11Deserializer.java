package io.tech1.framework.domain.jsons.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static io.tech1.framework.domain.constants.DatetimeConstants.DTF11;
import static io.tech1.framework.domain.utilities.time.LocalDateTimeUtility.getTimestamp;
import static io.tech1.framework.domain.utilities.time.LocalDateTimeUtility.parse;
import static java.time.ZoneOffset.UTC;

public class DatetimeTimestampUTCDTF11Deserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return getTimestamp(parse(jsonParser.getText(), DTF11), UTC);
    }
}
