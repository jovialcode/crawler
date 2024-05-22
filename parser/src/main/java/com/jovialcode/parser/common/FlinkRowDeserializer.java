package com.jovialcode.parser.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jovialcode.parser.parsing.ParsingInfo;
import com.jovialcode.parser.parsing.ParsingRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlinkRowDeserializer<T> extends StdDeserializer<T> implements ContextualDeserializer {
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public FlinkRowDeserializer() {
        this(null);
    }

    public FlinkRowDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode dataNode = jp.getCodec().readTree(jp);

        if (this._valueClass == ParsingInfo.class) {
            // Deserialize root node into Data class
            ParsingInfo parsingInfo = new ParsingInfo();
            parsingInfo.setId(dataNode.get("id").asInt());
            parsingInfo.setCrawlerId(dataNode.get("crawler_id").asInt());

            // Handle parsing_rules
            String parsingRulesNode = dataNode.get("parsing_rules").textValue();
            List<ParsingRule> parsingRules = objectMapper.readValue(parsingRulesNode, new TypeReference<List<ParsingRule>>() {});
            parsingInfo.setParsingRules(parsingRules);

            return (T) parsingInfo;
        }

        // Default handling for other types
        return objectMapper.treeToValue(dataNode, ctxt.getTypeFactory().constructType(this._valueClass));
    }

    @Override
    public JsonDeserializer<T> createContextual(DeserializationContext ctxt, BeanProperty property) {
        return new FlinkRowDeserializer<>(property.getType().getRawClass());
    }
}
