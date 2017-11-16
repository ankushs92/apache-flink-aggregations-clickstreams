package com.adsizzler.adosiz.flink.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.val;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ankush on 02/03/17.
 */
public class UUIDDeserializer extends JsonDeserializer<UUID> {

    @Override
    public UUID deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        val text = parser.getText();
        return UUID.fromString(text);
    }
}
