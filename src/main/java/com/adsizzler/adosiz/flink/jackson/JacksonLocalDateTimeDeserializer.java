package com.adsizzler.adosiz.flink.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import in.ankushs.jvalet.utils.Strings;
import lombok.val;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Ankush on 04/02/17.
 */

public class JacksonLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(
            final JsonParser p,
            final DeserializationContext ctxt
    ) throws IOException
    {
        val text = p.getText();
        if(!Strings.hasText(text)){
            return null;
        }
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
