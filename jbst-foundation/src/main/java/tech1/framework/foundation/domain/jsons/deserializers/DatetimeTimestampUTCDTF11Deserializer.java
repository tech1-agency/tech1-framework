package tech1.framework.foundation.domain.jsons.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import tech1.framework.foundation.domain.constants.DatetimeConstants;

import java.io.IOException;

import static tech1.framework.foundation.utilities.time.LocalDateTimeUtility.getTimestamp;
import static tech1.framework.foundation.utilities.time.LocalDateTimeUtility.parse;
import static java.time.ZoneOffset.UTC;

public class DatetimeTimestampUTCDTF11Deserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return getTimestamp(parse(jsonParser.getText(), DatetimeConstants.DTF11), UTC);
    }
}
