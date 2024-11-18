package jbst.foundation.domain.jsons.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import jbst.foundation.domain.constants.DatetimeConstants;

import java.io.IOException;

import static java.time.ZoneOffset.UTC;
import static jbst.foundation.utilities.time.LocalDateTimeUtility.getTimestamp;
import static jbst.foundation.utilities.time.LocalDateTimeUtility.parse;

public class DatetimeTimestampUTCDTF31Deserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return getTimestamp(parse(jsonParser.getText(), DatetimeConstants.DTF31), UTC);
    }
}
